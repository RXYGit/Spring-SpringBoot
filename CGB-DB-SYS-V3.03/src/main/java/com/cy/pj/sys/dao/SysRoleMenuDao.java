package com.cy.pj.sys.dao;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysRoleMenuDao {
	/**
	 * 基于角色id获取角色对应的菜单id
	 * @param roleIds
	 * @return 角色对应的菜单id
	 */
	List<Integer> findMenuIdsByRoleIds(@Param("roleIds")Integer[] roleIds);

	 /**
	  * 基于角色id查询菜单id
	  * @param roleId
	  * @return
	  */
	 @Select("select menu_id from sys_role_menus where role_Id=#{roleId}")
	 List<Integer> findMenuIdsByRoleId(Integer roleId);
	 /**
	  * 写入角色和菜单关系数据
	  * @param roleId
	  * @param menuIds
	  * @return
	  */
	 int insertObjects(@Param("roleId")Integer roleId,
			           @Param("menuIds")Integer[] menuIds);
	
     /**
      * 基于菜单id删除角色和菜单关系数据
      * @param menuId
      * @return
      */
	 @Delete("delete from sys_role_menus where menu_id=#{menuId}")
	 int deleteObjectsByMenuId(Integer menuId);
	 /**
	  * 基于菜单id删除角色和菜单关系数据
	  * @param roleId
	  * @return
	  */
	 @Delete("delete from sys_role_menus where role_id=#{roleId}")
	 int deleteObjectsByRoleId(Integer roleId);
}








