package com.jt.pojo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown=true) //表示JSON转化时忽略未知属性(此处为images属性)
@TableName("tb_item")	//与表关联
@Data					//get/set/toString
@Accessors(chain=true)	//链式加载
public class Item extends BasePojo{ //父级实现了序列化接口.子级自动继承
	@TableId(type=IdType.AUTO)		//主键定义
	private Long id;				//商品id
	private String title;			//商品标题
	private String sellPoint;		//商品卖点信息
	private Long   price;			//商品价格  2位小数 * 100保存
		//1.dubbo计算有精度问题 0.11111+0.899999999=0.9999999	2.int>Long>Dubbo                      
	private Integer num;			//商品数量
	private String barcode;			//条形码
	private String image;			//商品图片信息   1.jpg,2.jpg,3.jpg
	private Long   cid;				//表示商品的分类id
	private Integer status;			//1正常，2下架
	
	//为了满足页面调用需求,添加get方法
	public String[] getImages(){
		
		return image.split(",");
	}
}
