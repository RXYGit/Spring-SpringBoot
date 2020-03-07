package com.jt.vo;

import java.util.List;

import com.jt.pojo.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

//为了实现EasyUI中表格数据展现,定义VO对象
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor	
public class EasyUITable {
	
	private Integer total;		//定义商品的总记录数
	private List<Item> rows;		//商品列表信息
	
	
}
