package com.jt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;

public interface ItemMapper extends BaseMapper<Item>{
	
	/**
	 * 高版本中可以省略@Param注解,参数名称必须一致.
	 * @param start
	 * @param rows
	 * @return
	 */
	@Select("SELECT * FROM tb_item ORDER BY updated DESC LIMIT #{start},#{rows}")
	List<Item> findItemByPage(int start,Integer rows);
	
	//sql: delete from tb_item where id in (id.....); xml配置
	//@Param作用:将参数封装为Map集合.  ids:[]值
	void deleteItems(Long[] ids);
	
}
