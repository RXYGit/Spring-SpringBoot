package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;
import com.cy.pj.sys.vo.SysUserMenuVo;

@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public List<SysUserMenuVo> findUserMenus(Integer userId) {
		//1.获取用户对应的角色idb并校验
		List<Integer> roleIds=
		sysUserRoleDao.findRoleIdsByUserId(userId);
		//2.基于角色id获取对应菜单id并校验
		List<Integer> menuIds=
	    sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[] {}));
		//3.基于菜单id获取菜单信息
		List<SysUserMenuVo> list=
		sysMenuDao.findUserMenus(menuIds.toArray(new Integer[] {}));
		return list;
	}
	
	/**
	 * @CacheEvict 描述方法时，表示此方法在执行时要清除缓存中的数据
	 * value的值menuCache为具体cache对象。
	 * allEntries=true表示要清除cache中所有数据
	 * beforeInvocation 表示是否在更新之前执行
	 */
	@CacheEvict(value = "menuCache",
			    allEntries = true,
			    beforeInvocation = false )
	@Override
	public int updateObject(SysMenu entity) {
		//1.参数校验
		Assert.isArgumentValid(entity==null, "保存对象不能为空");
		Assert.isEmpty(entity.getName(), "菜单名不能为空");
		//....
		//2.保存菜单信息
		int rows=sysMenuDao.updateObject(entity);//有可能网络中断,数据库连接没有了.
		//3.返回结果
		return rows;
	}
	@Override
	public int saveObject(SysMenu entity) {
		//1.参数校验
		Assert.isArgumentValid(entity==null, "保存对象不能为空");
		Assert.isEmpty(entity.getName(), "菜单名不能为空");
		//....
		//2.保存菜单信息
		int rows=sysMenuDao.insertObject(entity);//有可能网络中断,数据库连接没有了.
		//3.返回结果
		return rows;
	}
	
	@Override
	public List<Node> findZtreeMenuNodes() {
		return sysMenuDao.findZtreeMenuNodes();
	}
	@Override
	public int deleteObject(Integer id) {
		//1.参数校验
		Assert.isArgumentValid(id==null||id<1, "id值无效");
		//2.统计当前菜单对应的子菜单个数并校验
		int childCount=sysMenuDao.getChildCount(id);
		Assert.isServiceValid(childCount>0, "请先删除子元素");
		//3.删除菜单对应的关系数据
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		//4.删除菜单自身信息并校验
		int rows=sysMenuDao.deleteObject(id);
		Assert.isServiceValid(rows==0, "记录可能已经不存在");
		return rows;
	}
	/**
	 * Cacheable表示将查询到数据，存储到cache对象中，这个cache对象对应的名字为menuCache
	 * 问题：
	 * 1)请问menuCache中存储的value为谁?方法的返回值
	 * 2)请问menuCache中存储数据时使用的key是谁？
	 */
	@Cacheable(value = "menuCache")//这里的userCache对应一个缓存对象(地层实现map)
	//后续会追加扩展业务
	@Override
	public List<SysMenu> findObjects() {
		System.out.println("==menu.findObjects==");
		return sysMenuDao.findObjects();
	}

}








