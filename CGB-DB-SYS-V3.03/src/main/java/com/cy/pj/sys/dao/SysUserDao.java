package com.cy.pj.sys.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

@Mapper
public interface SysUserDao {
	@Update("update sys_users set password=#{password},salt=#{salt} where id=#{id}")
	int updatePassword(
			@Param("password")String password,
			@Param("salt")String salt,
			@Param("id")Integer id);

	/**
	 * 基于用户名(此名字来自于客户端用户的输入)查找用户对象
	 * @param username (用户名)，此数据在数据库中具备唯一性
	 * @return
	 */
	@Select("select * from sys_users where username=#{username}")
	SysUser findUserByUserName(String username);
	/**
	 * 基于用户id查询用户以及用户对应的部门信息
	 * @param id
	 * @return
	 */
	SysUserDeptVo findObjectById(Integer id);
	
	
	/**
	 * 更新用户信息
	 * @param entity
	 * @return
	 */
	int updateObject(SysUser entity);
	/**
	 * 添加用户信息
	 * @param entity
	 * @return
	 */
	int insertObject(SysUser entity);

	/**
	 * 禁用或启用用户对象
	 * @param id 用户id
	 * @param valid 状态值
	 * @param modifiedUser 修改用户(谁执行了这个操作-登录用户)
	 * @return
	 */
	@Update("update sys_users set valid=#{valid},modifiedUser=#{modifiedUser},modifiedTime=now() where id=#{id}")
	int validById(@Param("id")Integer id,@Param("valid")Integer valid,@Param("modifiedUser")String modifiedUser);
	 /**
     * 基于查询条件动态拼接sql查询用户信息
     * 1)username值为null或者""应查询所有用户信息
     * 2)username值不null或""应按输入的用户进行模糊查询.
     * @return 查询到的记录总数
     */
	int getRowCount(@Param("username")String username);
	/**
	 * 基于查询条件,获取当前页要呈现的记录信息
	 * @param username 查询条件
	 * @param startIndex 起始位置
	 * @param pageSize 页面大小(每页最多显示多少条记录)
	 * @return 当前页的记录
	 */
	List<SysUserDeptVo> findPageObjects(
			@Param("username")String username,//arg0,param1
			@Param("startIndex")Integer startIndex,//arg1,param2
			@Param("pageSize")Integer pageSize);//arg2,param3
	
}
