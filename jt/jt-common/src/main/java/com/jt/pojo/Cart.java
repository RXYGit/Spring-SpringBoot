package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_cart")
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BasePojo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@TableId(type = IdType.AUTO)
	private Long id;
	private Long userId;//用户编号
	private Long itemId;//商品编号
	private String itemTitle;//商品标题
	private String itemImage;//商品图片
	private Long itemPrice;//商品价格
	private Integer num; //商品数量
}
