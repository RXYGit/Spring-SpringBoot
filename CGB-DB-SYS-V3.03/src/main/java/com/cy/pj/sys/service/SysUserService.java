package com.cy.pj.sys.service;

import java.util.Map;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

public interface SysUserService {
	/**
	 * 修改用户密码
	 * @param password
	 * @param newPassword
	 * @param cfgPassword
	 * @return
	 */
	int updatePassword(String password,
	           String newPassword,
	           String cfgPassword);

	/**
	 * 基于用户id获取用户以及用户对应的部门信息,角色信息
	 * @param userId
	 * @return
	 */
	Map<String,Object> findObjectById(Integer userId);
	/**
	 * 保存用户自身信息以及对应的角色关系数据
	 * @param entity
	 * @param roleIds
	 * @return
	 */
	int saveObject(SysUser entity,Integer[] roleIds);
	/**
	 * 更新用户自身信息以及对应的角色关系数据
	 * @param entity
	 * @param roleIds
	 * @return
	 */
	int updateObject(SysUser entity,Integer[] roleIds);
	
	/**
	 * 禁用启用
	 * @param id
	 * @param valid
	 * @return
	 */
	int validById(Integer id,Integer valid);
	PageObject<SysUserDeptVo> findPageObjects(String username,Integer pageCurrent);
	
}
