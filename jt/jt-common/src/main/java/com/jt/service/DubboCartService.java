package com.jt.service;

import java.util.List;

import com.jt.pojo.Cart;


public interface DubboCartService {

	List<Cart> findCartListByUserId(Long userId);

	void saveCart(Cart cart);

	void updateNum(Cart cart);

	void deleteCart(Cart cart);

	
}
