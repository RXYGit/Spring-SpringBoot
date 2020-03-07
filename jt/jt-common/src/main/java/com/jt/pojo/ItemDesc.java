package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@TableName("tb_item_desc")
@Data //toString  只会显示自己的属性信息. 重写toString
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemDesc extends BasePojo{
	
	@TableId
	private Long itemId;	//商品表中的id信息应该一致
	private String itemDesc;
}




