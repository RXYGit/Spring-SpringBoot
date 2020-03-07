package com.cy.pj.sys.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.common.config.PaginationProperties;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@Service
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Autowired
	private PaginationProperties paginationProperties;
	
	@Override
	public List<CheckBox> findObjects() {
		return sysRoleDao.findObjects();
	}
	
	@Override
	public SysRoleMenuVo findObjectById(Integer id) {
		//1.参数校验
		Assert.isArgumentValid(id==null||id<1, "id值无效");
		//2.查询角色自身信息
		SysRoleMenuVo rm=sysRoleDao.findObjectById(id);
		//3.基于角色id查询角色对应的菜单id
		//List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleId(id);
		//rm.setMenuIds(menuIds);
		return rm;
	}

	
	@Override
	public int updateObject(SysRole entity, Integer[] menuIds) {
		//1.参数校验
		Assert.isArgumentValid(entity==null, "保存对象不能为空");
		Assert.isEmpty(entity.getName(), "角色名不能为空");
		//2.保存角色自身信息
		int rows=sysRoleDao.updateObject(entity);
		//3.保存角色菜单关系数据
		//3.1先删除原有关系数据
		sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
		//3.2添加新的关系数据
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		//4.返回结果
		return rows;
	}
	@Override
	public int saveObject(SysRole entity, Integer[] menuIds) {
		//1.参数校验
		Assert.isArgumentValid(entity==null, "保存对象不能为空");
		Assert.isEmpty(entity.getName(), "角色名不能为空");
		//2.保存角色自身信息
		int rows=sysRoleDao.insertObject(entity);
		//3.保存角色菜单关系数据
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		//4.返回结果
		return rows;
	}
	
	@Override
	public int deleteObject(Integer id) {
	    //1.参数校验
		Assert.isArgumentValid(id==null||id<1, "id值无效");
		//2.删除菜单角色关系数据
		sysRoleMenuDao.deleteObjectsByRoleId(id);
		//3.删除角色用户关系数据
		sysUserRoleDao.deleteObjectsByRoleId(id);
		//4.删除角色自身信息
		int rows=sysRoleDao.deleteObject(id);
		//5.返回结果
		return rows;
	}
	
	@Override
	public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
	    //1.参数校验
		Assert.isArgumentValid(pageCurrent==null||pageCurrent<1, "当前页码值不正确");
		//2.查询总记录数并校验
		int rowCount=sysRoleDao.getRowCount(name);
		Assert.isServiceValid(rowCount==0, "没有对应的记录");
		//3.查询当前页角色信息记录
		Integer pageSize=paginationProperties.getPageSize();
		Integer startIndex=paginationProperties.getStartIndex(pageCurrent);
		List<SysRole> records=
		sysRoleDao.findPageObjects(name, startIndex, pageSize);
		//4.封装查询结果
		return new PageObject<>(rowCount, records, pageSize, pageCurrent);
	}

}













