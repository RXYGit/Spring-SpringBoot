package com.cy.pj.sys.service;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;
/**
 * 日志模块的业务接口
 */
public interface SysLogService {
	 /**
	  * 保存用户行为日志
	  * @param entity
	  */
	 void saveObject(SysLog entity);
	
	 int deleteObjects(Integer ...ids);
	
     /**
      * 基于条件分页查询当前页的用户行为日志记录信息
      * @param username  用户名
      * @param pageCurrent 当前页码值
      * @return 页面要呈现的日志记录以及分页信息
      */
	 PageObject<SysLog> findPageObjects(
			 String username,
			 Integer pageCurrent);
}
