package com.cy.pj.sys.service;

import java.util.List;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.vo.SysUserMenuVo;

public interface SysMenuService {
	
	   
	   List<SysUserMenuVo> findUserMenus(Integer userId);
	   int updateObject(SysMenu entity);
	   int saveObject(SysMenu entity);
	   List<Node> findZtreeMenuNodes();
	   /**
	    * 查询所有菜单以及菜单对应的上级菜单
	    * @return
	    */
	   List<SysMenu> findObjects();
	   /**
	    * 基于菜单id删除菜单自身信息以及对应的关系数据
	    * @param id
	    * @return
	    */
	   int deleteObject(Integer id);
}
