package com.jt.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

public class TestRedis {
	
	//利用redis自己的API暂时不需要和Spring整合.
	//报错信息:程序链接不通.  
	// 1.关闭防火墙   service iptables stop
	// 2.检查redis.conf中的配置文件  ip绑定注释,关闭保护模式,后台启动
	// 3.redis-server redis.conf
	@Test
	public void testString() {
		//主机IP地址
		String host = "192.168.126.174";
		int port = 6379;	//主机端口号
		Jedis jedis = new Jedis(host, port);
		//redis中的操作命令就是调用的方法.
		String result = jedis.set("1910","1910班,抗击疫情,人人有责");
		System.out.println("结果为:"+result);
		String value = jedis.get("1910");
		System.out.println("获取value:"+value);
	}
	
	
	Jedis jedis = null;
	
	@BeforeEach	 //当执行test测试方法执行之前
	public void init() {
		String host = "192.168.126.174";
		int port = 6379;	//主机端口号
		jedis = new Jedis(host, port);
	}
	/**
	 * 	业务说明:
	 * 	 如果redis中已经存在了这个key,则不允许赋值.
	 * 	 redis方法介绍:
	 * 		1.利用exists判断实现赋值.
	 * 		2.利用setnx方式进行判断
	 */
	@Test
	public void test02() {
		//1.判断一个该key是否存在.
		if(!jedis.exists("1910")) {
			jedis.set("1910", "测试赋值是否成功!!!");
		}
		System.out.println(jedis.get("1910"));
	}
	
	@Test
	public void test03() {
		jedis.set("", "");//后边的操作一定会覆盖之前的操作.
		//如果key不存在,则执行set操作,否则跳过.
		jedis.setnx("1910", "测试setnx");
		System.out.println("value值:"+jedis.get("1910"));
	}
	
	/**
	 * 为key添加超时时间
	 * @throws InterruptedException 
	 * 原子性操作: 要求赋值和超时时间的设定同时完成.
	 */
	@Test
	public void test04() throws InterruptedException {
		jedis.setnx("1910", "超时时间的测试");
		jedis.expire("1910", 20);	//添加超时时间
		//暂时不是原子性操作.
		Thread.sleep(3000);
		Long seconds =jedis.ttl("1910");
		System.out.println("剩余存活时间:"+seconds);
	}
	
	//实现原子性操作
	@Test
	public void test05() throws InterruptedException {
		//要么同时成功,要么同时失败.
		jedis.setex("1910", 20, "测试原子性");
		System.out.println(jedis.ttl("1910"));
	}
	
	/**
	 * 要求: 进行操作和添加超时时间同时完成.并且不允许修改已有数据.
	 * 问题说明:
	 * 		setnx:可以实现不修改已有数据.
	 * 		setex:可以实现赋值和超时时间操作.
	 * 		但是上述操作不能同时完成.要求时原子性的
	 * 
	 *  String XX = "xx";  完成赋值.不管之间
  		String NX = "nx";  如果存在,不赋值.
  		String PX = "px";  超时时间单位毫秒
  		String EX = "ex";  超时时间单位秒
	 */
	@Test
	public void test06() {
		SetParams params = new SetParams();
		params.nx().ex(60);
		//满足赋值和超时的原子性操作.
		jedis.set("1910", "String类型终极测试33333322", params);
		System.out.println(jedis.get("1910"));
	}
	
	
	/**
	 * 操作hash数据类型,保存对象的数据.
	 * 说明:如果有一类数据需要redis保存时,可以使用hash.
	 * userId,username,addr
	 */
	@Test
	public void testHash() {
		jedis.hset("person", "id", "101");
		jedis.hset("person", "name", "小甜甜");
		jedis.hset("person", "age", "80");
		Map<String,String> personMap = jedis.hgetAll("person");
		System.out.println(personMap);
	}
	
	/**
	 * 	测试redis的队列操作.
	 * 	需求将队列当做栈.
	 */
	@Test
	public void testList() {
		
		jedis.lpush("list", "1","2","3","4");
		System.out.println(jedis.lpop("list")); //5
	}
	
	/**
	 * redis中事务 redis中的事务是弱事务.
	 * 使用redis事务一般不会级联.
	 */
	@Test
	public void testTX() {
		//1.开启事务控制
		Transaction transaction = jedis.multi();
		try {
			transaction.set("a", "a");
			transaction.set("b", "b");
			transaction.exec();
			System.out.println(jedis.get("a"));
		} catch (Exception e) {
			e.printStackTrace();
			transaction.discard();
		}
		//将上述操作交给spring容器的AOP管理.
	}
	
	
	/**
	 * 每个redis中保存的数据 一定不相同.
	 * 数据如何存储的?
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.126.174",6379));
		shards.add(new JedisShardInfo("192.168.126.174",6380));
		shards.add(new JedisShardInfo("192.168.126.174",6381));
		ShardedJedis shardedJedis = new ShardedJedis(shards);
		shardedJedis.set("1910","分片测试!!!!");
		System.out.println(shardedJedis.get("1910"));
	}
	
	
	/**
	 * 实现Redis哨兵的配置
	 */
	@Test
	public void testSentinel() {
		String masterName = "mymaster";
		//准备哨兵的集合配置  IP:PORT
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.126.174:26379");
		JedisSentinelPool sentinelPool = 
					new JedisSentinelPool(masterName, sentinels);
		Jedis jedis = sentinelPool.getResource(); //从池中动态获取
		jedis.set("1910","哨兵操作正确!!!!!");
		System.out.println(jedis.get("1910"));
		jedis.close();
	}
	
	
	/**
	 * 实例化集群对象
	 */
	@Test
	public void testCluster() {
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.126.174", 7000));
		nodes.add(new HostAndPort("192.168.126.174", 7001));
		nodes.add(new HostAndPort("192.168.126.174", 7002));
		nodes.add(new HostAndPort("192.168.126.174", 7003));
		nodes.add(new HostAndPort("192.168.126.174", 7004));
		nodes.add(new HostAndPort("192.168.126.174", 7005));
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("1910", "redis集群操作成功!!!!");
		System.out.println(cluster.get("1910"));
	}
	
	
	
	
	
}
