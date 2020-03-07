package com.jt.service;

import java.util.List;

import com.jt.vo.EasyUITree;

public interface ItemCatService {

	String findItemCatService(Long itemCatId);
	//自下而上 需要大家对整个业务有一个宏观把控 脑内存容量大
	//自上而下 以目标为指导,逐步完成. Controller-service-mapper

	List<EasyUITree> findItemCatListByParentId(Long parentId);
	//根据缓存查询数据.
	List<EasyUITree> findItemCatByCache(Long parentId);
}
