package com.cy.pj.sys.dao;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.vo.SysUserMenuVo;
@Mapper
public interface SysMenuDao {
	
	 /**
	  * 基于指定菜单id查询菜单信息
	  * @param menuIds
	  * @return
	  */
	 List<SysUserMenuVo> findUserMenus(Integer[] menuIds);//@Param("menuIds")
	
	/**
	 * 基于菜单id获取菜单授权标识(sys:user:update,sys:role:view,...)
	 * @param menuIds
	 * @return
	 */
	  List<String> findPermissions(@Param("menuIds") Integer[] menuIds);

	/**
	 * 更新菜单信息	
	 * @param entity po-->持久化对象
	 * @return
	 */
	  int updateObject(SysMenu entity);
       /**
        * 持久化菜单信息	
        * @param entity po-->持久化对象
        * @return
        */
	   int insertObject(SysMenu entity);
	   /**
	    * 查询菜单节点信息(id,name,parentId)
	    * @return
	    */
	   @Select("select id,name,parentId from sys_menus")
	   List<Node> findZtreeMenuNodes();
	
	   /**
	    * 基于菜单id统计其子元素的个数
	    * @param id
	    * @return
	    */
	   @Select("select count(*) from sys_menus where parentId=#{id}")
	   int getChildCount(Integer id);
	   
	   /**
	    * 基于id删除当前菜单对象
	    * @param id
	    * @return
	    */
	   @Delete("delete from sys_menus where id=#{id}")
	   int deleteObject(Integer id);
	   
	   /**
	    * 查询所有菜单以及菜单对应的上级菜单id和名称
	    * @return 所有菜单对应的一个集合(一行记录映射为一个map,map中key字段名)
	    * 为什么使用map?有什么优势,劣势?
	    * 第一:map本是一个容器,可以以key/value的形式存储数据
	    * 第二:通过map存储数据,可以省略我们自己对pojo对象的定义,这也是最大的优势.
	    * 第三:使用map作为pojo对象存储数据,最大的缺陷是值的类型不可控 ,并且打开
	    * map源码不知道map中存储了什么数据(可读性差).
	    */
	    List<SysMenu> findObjects();
	   
}
