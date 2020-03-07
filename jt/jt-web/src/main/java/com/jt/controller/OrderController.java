package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Reference
	private DubboCartService dubboCartService;
	@Reference
	private DubboOrderService dubboOrderService;
	/**
	 * 跳转订单确认页面
	 * url:http://www.jt.com/order/create.html
	 * 根据userId查询购物车数据信息,页面${carts}
	 */
	@RequestMapping("/create")
	public String create(Model model) {
		
		Long userId = ThreadLocalUtil.getUser().getId();
		List<Cart> cartList = dubboCartService.findCartListByUserId(userId);
		model.addAttribute("carts",cartList);
		return "order-cart";
	}
	
	/**
	 * url  :/order/submit
	 * 参数: 整个form表单
	 * 返回值: SysResult对象
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order) {
		
		Long userId = ThreadLocalUtil.getUser().getId();
		order.setUserId(userId);
		
		String orderId = dubboOrderService.saveOrder(order);
		if (StringUtils.isEmpty(orderId)) {
			return SysResult.fail();
		}
		return SysResult.success(orderId);
	}
	
	/**
	 * 根据orderId查询订单信息
	 * url:http://www.jt.com/order/success.html?id=91582967870718
	 * 参数:id=xxxxx
	 * 返回值:success.html  成功页面
	 * 页面的取值:${order.orderId}
	 */
	@RequestMapping("/success")
	public String findOrderById(String id,Model model) {
		
		Order order =  dubboOrderService.findOrderById(id);
		model.addAttribute("order",order);
		return "success";
	}
}





