package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserRoleDao {
	

	/**
	 * 基于用户id查询用户对应的角色id
	 * @param id
	 * @return
	 */
	@Select("select role_id from sys_user_roles where user_id=#{userId}")
	List<Integer> findRoleIdsByUserId(Integer userId);
	
	/**
	 * 添加用户和角色关系数据
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	int insertObjects(@Param("userId")Integer userId,
			          @Param("roleIds")Integer[] roleIds);
	/**
	 * 用户和角色是多对多的关系(Many2Many关系需要借助中间表维护关系)
	 * 基于用户id删除用户和角色关系数据
	 * @param roleId
	 * @return
	 */
	@Delete("delete from sys_user_roles where user_id=#{userId}")
	int deleteObjectsByUserId(Integer userId);
	/**
	 * 用户和角色是多对多的关系(Many2Many关系需要借助中间表维护关系)
	 * 基于角色id删除用户和角色关系数据
	 * @param roleId
	 * @return
	 */
	@Delete("delete from sys_user_roles where role_id=#{roleId}")
	int deleteObjectsByRoleId(Integer roleId);
	
}
