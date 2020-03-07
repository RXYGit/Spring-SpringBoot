package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service
public class DubboCartServiceImpl implements DubboCartService {

	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		
		return cartMapper.selectList(queryWrapper);
	}

	/**
	 * 购物车新增
	 * 新增购物车时,根据itemId和userId 查询购物车数据
	 * 		如果查询的结果为null,表示用户第一次新增该商品,则直接入库
	 * 		非null,表示用户之前购买过该商品,则更新商品数量
	 */
	@Override
	public void saveCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("item_id", cart.getItemId())
					.eq("user_id", cart.getUserId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);//数据库记录
		if (cartDB == null) {
			//用户第一次购买
			cart.setCreated(new Date())
				.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			//用户不是第一次购买,只做数量的更新
			//实质修改的数据num/updated   
			int num = cartDB.getNum() + cart.getNum();
			Cart cartTemp = new Cart();
			cartTemp.setNum(num).setId(cartDB.getId()).setUpdated(new Date());
			cartMapper.updateById(cartTemp);		
			
			//UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
			//updateWrapper.eq("id", cartDB.getId());
			//cartMapper.update(entity, updateWrapper);
			
			
			//cartDB.setNum(num).setUpdated(new Date());
			//cartMapper.updateById(cartDB);
			
		}
		
	}

	/**
	 * 购物车的数量的更新
	 * 参数:userId  itemId(唯一确定用户信息)  num
	 */
	@Override
	public void updateNum(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
		updateWrapper.eq("item_Id", cart.getItemId())
					.eq("user_Id", cart.getUserId());
		cartMapper.update(cartTemp, updateWrapper);
	}

	/**
	 * 购物车的删除
	 * cart只有userId和itemId不为null
	 * 对象中不为null的属性充当where条件
	 */
	@Override
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		cartMapper.delete(queryWrapper);
	}
	
	
	
}





