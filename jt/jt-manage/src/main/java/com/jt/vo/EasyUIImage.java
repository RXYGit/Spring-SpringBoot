package com.jt.vo;

import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//定义图片返回的VO对象.
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUIImage {
	private Integer error; //0表示正常  1失败
	private String url;
	private Integer width;
	private Integer height;
	
	//1.失败方法
	public static EasyUIImage fail() {
		
		return new EasyUIImage(1, null, null, null);
	}
	
	//2.成功方法
	public static EasyUIImage success(String url,Integer width,Integer height) {
		
		return new EasyUIImage(0, url, width, height);
	}
}


