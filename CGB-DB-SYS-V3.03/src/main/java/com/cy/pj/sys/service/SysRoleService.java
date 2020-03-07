package com.cy.pj.sys.service;

import java.util.List;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

public interface SysRoleService {
	
	List<CheckBox> findObjects();
	
	/**
	 * 基于id查询角色自身信息以及角色对应的菜单id
	 * @param id
	 * @return
	 */
	SysRoleMenuVo findObjectById(Integer id);
	/**
	 * 更新角色自身信息以及角色对应的菜单信息
	 * @param entity
	 * @param menuIds
	 * @return
	 */
	int updateObject(SysRole entity,Integer[]menuIds);
     /**
      * 保存角色自身信息以及角色对应的菜单信息
      * @param entity
      * @param menuIds
      * @return
      */
	 int saveObject(SysRole entity,Integer[]menuIds);
	 /**
	  * 基于角色id删除角色自身信息以及角色对应的关系数据
	  * @param id
	  * @return
	  */
	 int deleteObject(Integer id);
	 /**
	  * 分页查询当前页角色记录信息以及分页数据
	  * @param name
	  * @param pageCurrent
	  * @return
	  */
	 PageObject<SysRole> findPageObjects(String name,Integer pageCurrent);
}
