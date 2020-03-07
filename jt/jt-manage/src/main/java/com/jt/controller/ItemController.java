package com.jt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

//实现商品的业务逻辑
@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 关于SpringMVC页面取值复习.
	 * 1.页面标签
	 * 	 <input  name="page" value="1"  />
	 * 	 <input  name="rows" value="20"  />  提交request对象
	 * 2.SpringMVC底层DispatcherServlet.底层实现依然采用servlet的方式动态的获取
	 * 	 数据.可以利用request对象的方式获取数据.
	 * 	request.getParameter("xxx");
	 *  传参说明: 用户传递参数之后,服务器取数据时,按照指定的要求获取数据.如果key不同,
	 *  那么获取到的数据为null.而不报错.
	 *  规则:SpringMVC中参数的名字,必然和页面中的name属性相同.
	 *  
	 * 3.SpringMVC将上述操作简化. 并且可以自动的实现数据类型的切换
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/query")   //?page=1&rows=20 get请求
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		
		return itemService.findItemByPage(page,rows);
	}
	
	/**
	 * 实现商品新增
	 * url:/item/save
	 *   参数: 提交的是整个表单数据.title=xxx&price=xxx&....
	 *   返回值: SysResult对象 
	 *   String itemDesc
	 */
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		//业务解耦
		itemService.saveItem(item,itemDesc); //100%
		return SysResult.success(); //统一异常处理
	}
	
	/**
	 * url: /item/update
	 *   参数:  页面表格参数
	 *   返回值: SysResult对象
	 * @return
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	
	/**
	 * 1.url地址 /item/instock
	 * 2.参数: ids:100,101,102
	 * 3.返回值: 返回VO对象SysResult
	 * 下架:操作将status 改为2,同时修改updated时间
	 * 规则: 如果字符串以","进行分割,springMVC可以自动的拆分转化为数组      
	 */
	@RequestMapping("/instock")	
	public SysResult itemInstock(Long[] ids) {
		
		int status = 2;	//下架操作
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}
	
	///item/reshelf
	@RequestMapping("/reshelf")	//状态改为1
	public SysResult itemReshelf(Long[] ids) {
		
		int status = 1;	//上架操作
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}
	
	/**
	 * url:/item/delete
	 * 参数: ids=101,102,103
	 * 返回值结果
	 */
	@RequestMapping("/delete")
	public SysResult itemDeletes(Long[] ids) {
		
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	
	
	/**
	 * url: /item/query/item/desc/1474392154
	 * 参数:采用restFul方式实现参数的传递
	 * 返回值: SysResult对象
	 * 业务: 根据itemId查询商品详情.
	 * @return
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {
		
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		return SysResult.success(itemDesc);
	}
	
	
	
	
	
	
	
	
	
	
}
