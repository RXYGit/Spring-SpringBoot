package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
	
	//1.定义Mapper对象
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//2.对象转化为JSON
	public static String toJSON(Object obj){
		//异常转化规则:将检查异常转化为运行时异常
		String result = null;
		try {
			result = MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return result;
	}
	
	//3.JSON串转化为对象
	//需求:用户传递了什么类型,就需要返回什么对象.
	//自定义<T> 
	public static <T> T toObj(String json,Class<T> targetClass){
		
		T t = null;
		try {
			t = MAPPER.readValue(json, targetClass);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return t;
	}
}
