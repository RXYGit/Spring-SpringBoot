package com.cy.pj.sys.entity;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/**
 * POJO:(普通的java对象)
 * 1)PO(持久化对象):与表中字段有一一映射关系
 * 2)VO(值对象-value object) 用于封装业务数据(一般和表中字段没有一一映射关系)
 * 3)....
 */
@Data
public class SysLog implements Serializable{
	//序列化的版本id,没有此id,在序列化时也会基于类的结构自动生成id,
	//但是一旦类的结构发生变化,假如此id没有显式声明,可以反序列化会出现异常.
	private static final long serialVersionUID = 3820536062098779606L;
	private Integer id;
	//用户名
	private String username;
	//用户操作
	private String operation;
	//请求方法
	private String method;
	//请求参数
	private String params;
	//执行时长(毫秒)
	private Long time;
	//IP地址
	private String ip;
	//创建时间
	private Date createdTime;
}
