package com.jt.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class TestJSON {
	
	
	
	/**
	 * 1.说明:由对象转化为JSON
	 * @throws JsonProcessingException 
	 */
	private static final ObjectMapper MAPPER = new ObjectMapper();
	@Test
	public void test2JSON() throws JsonProcessingException {
		//1.对象转化为JSON
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1000L).setItemDesc("JSON测试");
		String json = MAPPER.writeValueAsString(itemDesc);
		System.out.println(json);
		
		//2.将json串转化为对象  反射原理
		ItemDesc itemDesc2 = MAPPER.readValue(json, ItemDesc.class);
		System.out.println(itemDesc2);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void list2JSON() throws JsonProcessingException {
		//1.对象转化为JSON
		List<ItemDesc> itemDescList = new ArrayList<ItemDesc>();
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(1000L).setItemDesc("JSON测试");
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(1000L).setItemDesc("JSON测试");
		itemDescList.add(itemDesc1);
		itemDescList.add(itemDesc2);
		String json = MAPPER.writeValueAsString(itemDescList);
		System.out.println(json);
		//2.将json串转化为List  反射原理
		List<ItemDesc> list = MAPPER.readValue(json,itemDescList.getClass());
		System.out.println(list);
	}
	
}
