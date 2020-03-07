package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

//标识配置类
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	
	/**
	 * springBoot整合Redis集群
	 */
	@Value("${redis.clusters}")
	private String jedisClusters;
	
	@Bean
	//@Scope("prototype")
	public JedisCluster jedisCluster() {
		Set<HostAndPort> setNodes = new HashSet<HostAndPort>();
		String[] nodes = jedisClusters.split(",");
		for (String node : nodes) {
			String host = node.split(":")[0];
			Integer port =Integer.parseInt(node.split(":")[1]);
			HostAndPort hostAndPort = new HostAndPort(host, port);
			setNodes.add(hostAndPort);
		}
		
		return new JedisCluster(setNodes);
	}
	
}	
	
	
	
	
	
	
	
	/*
	//配置redis单台
	@Value("${redis.node}")
	private String redisNode;   //IP:PORT
	
	@Bean	//标识实例化对象的类型
	@Scope("prototype")	//对象的多例  使用链接池
	public Jedis jedis() { //key:value  jedis:jedis对象
		
		String[] nodeArray = redisNode.split(":");
		String host = nodeArray[0];
		int port = Integer.parseInt(nodeArray[1]); 
		return new Jedis(host, port);
	}
	
	@Value("${redis.shards}")
	private String redisShards;	//node,node,node
	//2.配置redis分片机制.
	@Bean
	@Scope("prototype")
	public ShardedJedis shardedJedis() {
		String[] nodeArray = redisShards.split(",");
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		for (String node : nodeArray) { //node=IP:PORT
			String[] nodeArr = node.split(":");
			String host = nodeArr[0];
			int port = Integer.parseInt(nodeArr[1]);
			//每循环一次,添加一个node节点对象到list集合中
			shards.add(new JedisShardInfo(host, port));
		}
		return new ShardedJedis(shards);
	}
	
	
	*//**
	 * 3.整合redis的哨兵
	 * 创建哨兵的池对象.
	 *//*
	@Value("${redis.sentinel}")
	private String redisSentinel;
	
	@Bean   //(name="pool") //给bean 动态的起名.
	public JedisSentinelPool jedisSentinelPool() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add(redisSentinel);
		return new JedisSentinelPool("mymaster", sentinels);
	}
	
	// 动态获取池中的jedis对象
	//问题说明:如何在方法中,动态获取bean对象.
	//知识点说明: 
	//    1.Spring @Bean注解工作时,如果发现方法有参数列表.则会自动的注入.
	//    2.@Qualifier 利用名称,实现对象的动态赋值.
		
	//sentinelJedis:jedis对象
	@Bean
	@Scope("prototype")	//设置为多例,用户什么时候使用,什么时候创建对象
	public Jedis sentinelJedis(JedisSentinelPool jedisSentinelPool) {
		
		//该jedis 有高可用的效果.
		return  jedisSentinelPool.getResource();
	}
	*/
	
	
	
	
	
	
	

