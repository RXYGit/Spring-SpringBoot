package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.annotation.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	//@Autowired	
	private Jedis jedis; //注入redisAPI对象

	@CacheFind
	@Override
	public String findItemCatService(Long itemCatId) {
		
		//查询商品分类对象
		ItemCat itemCat = itemCatMapper.selectById(itemCatId);
		//获取商品分类名称
		return itemCat.getName();
	}

	/*查询数据库记录,之后实现商品分类展现
	 * 业务分析:
	 * 	1.要求返回List<EasyUITree> 数据.
	 * 	2.需要将itemCatList集合转化为List<VO>对象
	 * 	2.itemCatMapper.selectList(); 查询的是数据库itemCat数据.
	 * */
	@Override
	@CacheFind   //查询的注解
	public List<EasyUITree> findItemCatListByParentId(Long parentId) {
		
		//1.根据parentID查询数据库记录.
		List<ItemCat> itemCatList = findItemCatList(parentId);
		
		//2.itemCatList~~~~List<EasyUITree>
		List<EasyUITree> treeList = new ArrayList<>(itemCatList.size());
		
		for (ItemCat itemCat : itemCatList) {
			Long id = itemCat.getId();
			String text = itemCat.getName();
			//如果是父级 默认closed,否则开启open
			String state = itemCat.getIsParent()?"closed":"open";
			EasyUITree uiTree = new EasyUITree(id, text, state);
			treeList.add(uiTree);
		}
		
		return treeList;
	}
	
	//根据parentId查询分类信息
	private List<ItemCat> findItemCatList(Long parentId) {
		
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}

	/**
	 *	1.需要注入jedis对象
	 *	2.定义key  包名.类名.方法名::第一个参数
	 *	3.一开始先查询缓存,如果缓存中有数据则执行缓存业务.
	 *	  如果缓存中没有数据则查询数据库.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EasyUITree> findItemCatByCache(Long parentId) {
		Long startTime = System.currentTimeMillis();
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		String key = "com.jt.service.ItemCatServiceImpl.findItemCatByCache::"+parentId;
		String value = jedis.get(key);	//根据key,查询值
		if(StringUtils.isEmpty(value)) {
			//缓存中没有数据,查询数据库.需要将数据存入redis.
			treeList = findItemCatListByParentId(parentId);
			String json = ObjectMapperUtil.toJSON(treeList);
			//将数据保存到redis中.
			jedis.set(key, json);
			Long dbTime = System.currentTimeMillis();
			System.out.println("查询数据库执行时间:"+(dbTime-startTime)+"毫秒");
		}else {
			//缓存中有数据,需要将JSON转化为对象
			treeList = ObjectMapperUtil.toObj(value, treeList.getClass());
			Long cacheTiemt = System.currentTimeMillis();
			System.out.println("查询缓存时间:"+(cacheTiemt-startTime)+"毫秒");
		}
		return treeList;
	}

}
