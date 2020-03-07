package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@Controller
@RequestMapping("/items")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	/**
	 * 
	 *    用户前台动态获取商品信息.
	 *  1.url:http://www.jt.com/items/562379.html
	 *  2.用户接收url请求之后,动态获取商品信息(item+itemDesc)
	 *  3.获取数据之后,在item.jsp页面中进行展现.
	 *  4.取值方式: ${item.title},${itemDesc.itemDesc }
	 */
	@RequestMapping("/{itemId}")
	public String findItemById(@PathVariable Long itemId,Model model) {
		
		Item item = itemService.findItemById(itemId);
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		//将数据保存到域中  request域.
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";	//页面逻辑名称
	}
}
