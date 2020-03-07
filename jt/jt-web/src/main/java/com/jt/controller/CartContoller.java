package com.jt.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartContoller {

	@Reference(check = false)
	private DubboCartService cartService;
	/**
	 * 实现购物车的页面跳转
	 * url:http://www.jt.com/cart/show.html
	 * 页面的取值:在cart.jsp中通过${cartList}
	 */
	@RequestMapping("/show")
	public String show(Model modle) {
		
	//public String show(Model modle,HttpServletRequest request) {
		//查询购物页面信息    1:动态获取userID(暂时写固定的7L)  2:通过jt-cart后台项目动态获取购物车记录
		//3:将数据展现到页面中
//		User  user = (User) request.getAttribute("JT_USER");
//		Long userId = user.getId();
		
		
		Long userId = ThreadLocalUtil.getUser().getId();
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		modle.addAttribute("cartList", cartList);
		return "cart";
	}
	
	/**
	 * 实现购物车的新增
	 * url:http://www.jt.com/cart/add/562379.html
	 * 参数:form表单提交  itemId ,itemTitle......等
	 * 		参数接收:如果参数名称与属性相同,则直接用对象转化,并且@PathVariable可以不写
	 * 返回值:返回页面逻辑名称  重定向到购物车展现的页面
	 */
	@RequestMapping("/add/{itemId}")
	public String savaCart(Cart cart) {
		
		Long userId = ThreadLocalUtil.getUser().getId();
		cart.setUserId(userId);
		cartService.saveCart(cart);
		
		return "redirect:/cart/show.html";
	}
	
	/**
	 * 实现购物车商品数量的更新
	 * url:http://www.jt.com./cart/update/num/562379/24
	 * 参数:itemId,num
	 * 返回值:SysResult对象
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(Cart cart) {
		
		Long userId = ThreadLocalUtil.getUser().getId();
		cart.setUserId(userId);
		cartService.updateNum(cart);
		return SysResult.success();
	}
	
	/**
	 * 实现购物车的删除
	 * 跳转url:重定向购物车列表页面:http://www.jt.com/delete/1474392156.html
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart) {
		
		Long userId = ThreadLocalUtil.getUser().getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show.html";
	}
}





