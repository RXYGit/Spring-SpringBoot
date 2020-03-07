package com.cy.pj.sys.vo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
/**
 * 基于此对象封装角色修改页面上要呈现的数据
 * 1)角色自身信息:id,name,note  (来自于角色表:sys_roles)
 * 2)角色对应的菜单信息:menuId (来自于角色菜单关系表:sys_role_menus)
 * 如上数据应该来与对数据库数据的查询,具体查询方案:
 * 1)单表查询:(在业务层执行两次数据查询操作,最终在业务层进行数据封装)
 * 2)嵌套查询:(数据库库端可能要发两次SQL)
 * 3)多表查询:(sys_role 关联 sys_role_menus)
 */
@Data
public class SysRoleMenuVo implements Serializable{
	private static final long serialVersionUID = -7213694248989299601L;
	private Integer id;
	private String name;
	private String note;
	private List<Integer> menuIds;
}






