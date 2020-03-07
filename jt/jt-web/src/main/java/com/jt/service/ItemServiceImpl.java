package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.annotation.CacheFind;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class ItemServiceImpl implements ItemService {

	//由jt-web调用jt-manage后台服务器  httpClient工具api
	@Autowired
	private HttpClientService httpClient;
	
	
	@CacheFind
	@Override
	public Item findItemById(Long itemId) {
		String url = "http://manage.jt.com/web/item/findItemById";
		Map<String,String> params = new HashMap<String, String>();
		params.put("itemId", itemId+"");
		String itemJson = httpClient.doGet(url, params);
		return ObjectMapperUtil.toObj(itemJson, Item.class);
	}

	@CacheFind
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		String url = "http://manage.jt.com/web/item/findItemDescById";
		Map<String,String> params = new HashMap<String, String>();
		params.put("itemId", itemId+"");
		String itemDescJSON = httpClient.doGet(url, params);
		return ObjectMapperUtil.toObj(itemDescJSON,ItemDesc.class);
	}
}
