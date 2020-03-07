package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@Mapper
public interface SysRoleDao {
	
	 /**
	  * 查询所有角色id,name,每条记录封装为一个checkbox对象
	  * @return
	  */
	 @Select("SELECT id,name FROM sys_roles")
	 List<CheckBox> findObjects();
	
	 SysRoleMenuVo findObjectById(Integer id);
	
	 /**
	  * 写入角色自身信息
	  * @param entity
	  * @return
	  */
	 int updateObject(SysRole entity);
	 /**
	  * 写入角色自身信息
	  * @param entity
	  * @return
	  */
	 int insertObject(SysRole entity);
	
	 /**
	  * 删除角色自身信息
	  * @param id
	  * @return
	  */
	 @Delete("delete from sys_roles where id=#{id}")
	 int deleteObject(Integer id);

	 /**
	  * 基于条件查询角色总数
	  * @param name 角色名称
	  * @return
	  */
	 int getRowCount(@Param("name")String name);
	 /**
	  * 查询当前页记录
	  * @param name 角色名称
	  * @param startIndex 起始位置
	  * @param pageSize 页面大小
	  * @return
	  */
	 List<SysRole> findPageObjects(@Param("name")String name,
			 @Param("startIndex")Integer startIndex,
			 @Param("pageSize")Integer pageSize);
}
