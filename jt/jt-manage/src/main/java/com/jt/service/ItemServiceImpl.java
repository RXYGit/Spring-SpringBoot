package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	/**
	 * 分页查询数据
	 */
	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		/**
		 * sql:
		 * 		select * from tb_item limit 起始位置,查询记录数     每页20条
		 * 	第一页:
		 * 		select * from tb_item limit 0,20;   index:0-19 第21条没有取
		 *      第二页:
		 *      select * from tb_item limit 20,20;   
		 *      第三页  
		 *      select * from tb_item limit 40,20;
		 *      第N页
		 *      select * from tb_item limit (page-1)rows,rows;
		 */
		//1.手写分页
		/*int total = itemMapper.selectCount(null);	//查询总记录数
		int start = (page - 1) * rows;	//定义起始位置
		List<Item> itemList = itemMapper.findItemByPage(start,rows);*/
		
		//2.利用MP方式实现分页查询
		IPage<Item> iPage = new Page<>(page, rows);	//查询页数和条数
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("updated");
		IPage<Item> resultPage = itemMapper.selectPage(iPage, queryWrapper);
		//查询总页数
		int total = (int) resultPage.getTotal();
		//查询分页信息
		List<Item> itemList = resultPage.getRecords();
		return new EasyUITable(total, itemList);
	}

	@Override
	public void saveItem(Item item) {
		
		item.setStatus(1)  //上架状态
			.setCreated(new Date())
			.setUpdated(item.getCreated()); //时间相同
		itemMapper.insert(item);	//利用mp直接入库
	}
	
	@Override
	public void updateItem(Item item) {
		
		item.setUpdated(new Date());
		itemMapper.updateById(item);
	}

	//update 根据指定的id,修改指定的状态
	//sql: update tb_item set  status=#{status},updated=#{date}
	//	   where id in (id1,id2,id3....)
	//MP参数说明: entity:修改数据的值,updateWrapper 修改条件构造器
	@Override
	public void updateStatus(Long[] ids, Integer status) {
		Item item = new Item();
		item.setStatus(status)
			.setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<Item>();
		//将数组转化为List集合
		List<Long> idList = Arrays.asList(ids);
		updateWrapper.in("id", idList);
		itemMapper.update(item, updateWrapper);
	}
	
	@Transactional
	@Override
	public void deleteItems(Long[] ids) {
		//1.利用MP方式实现数据传递
		//List<Long> idList = Arrays.asList(ids);
		//itemMapper.deleteBatchIds(idList);
		
		//2.利用xml配置文件,实现数据的删除. 切记不用与MP方法重名
		itemMapper.deleteItems(ids);
		//利用MP实现ItemDesc数据的删除.
		List<Long> idList = Arrays.asList(ids);
		itemDescMapper.deleteBatchIds(idList);
		
	}

	/**
	 * 业务说明:
	 * 	同时新增2张表数据. tb_item与tb_item_desc主键Id相同
	 */
	@Override
	@Transactional	//控制事务
	public void saveItem(Item item, ItemDesc itemDesc) {
		//ID主键自增,所以现在没有id,id=null
		item.setStatus(1)  //上架状态
			.setCreated(new Date())
			.setUpdated(item.getCreated()); //时间相同
		itemMapper.insert(item); 
		//MP操作时,当item入库之后会将主键自动的回显.
		
		itemDesc.setItemId(item.getId())
				.setCreated(item.getCreated())
				.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemDescMapper.selectById(itemId);
	}

	@Transactional	//添加事务控制
	@Override
	public void updateItem(Item item, ItemDesc itemDesc) {
		
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		itemDesc.setItemId(item.getId())
				.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	@Override
	public Item findItemById(Long itemId) {
		
		return itemMapper.selectById(itemId);
	}
	
	
	
	
	
	
	
	
}
