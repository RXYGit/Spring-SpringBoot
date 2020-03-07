package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.config.PaginationProperties;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

@Service
public class SysLogServiceImpl implements SysLogService {
	@Autowired
	private SysLogDao sysLogDao;
	
	@Autowired
	private PaginationProperties paginationProperties;
	/**
	 * Propagation.REQUIRED 特性表示参与到一个已有的事务中去,假如没有已有的
	 * 则自己开启事务.
	 * Propagation.REQUIRES_NEW 特性表示此方法永远运行在一个新的事务中
	 */
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void saveObject(SysLog entity) {
		sysLogDao.insertObject(entity);
	}
	@Override
	public int deleteObjects(Integer... ids) {
		//1.参数校验
		//if(ids==null||ids.length==0)
			//throw new IllegalArgumentException("请选中记录进行删除");
		Assert.isArgumentValid(ids==null||ids.length==0, "请选中记录进行删除");
		//2.执行删除并校验结果
		int rows=sysLogDao.deleteObjects(ids);
		//if(rows==0)
		   //throw new ServiceException("记录可能已经不存在");
		Assert.isServiceValid(rows==0, "记录可能已经不存在");
		//3.返回结果
		return rows;
	}
	@Override
	public PageObject<SysLog> findPageObjects(String username, 
			Integer pageCurrent) {
		//1.参数校验
		//if(pageCurrent==null||pageCurrent<1)
			//throw new IllegalArgumentException("页码值不正确");
		Assert.isArgumentValid(pageCurrent==null||pageCurrent<1, "页码值不正确");
		//2.基于用户名查询总记录数并校验
		int rowCount=sysLogDao.getRowCount(username);
		//if(rowCount==0)
			//throw new ServiceException("没有找到对应记录");
		Assert.isServiceValid(rowCount==0, "没有找到对应记录");
		//3.查询当前页日志记录
		int pageSize=paginationProperties.getPageSize();//页面大小
		int startIndex=paginationProperties.getStartIndex(pageCurrent);
		List<SysLog> records=
		sysLogDao.findPageObjects(username, startIndex, pageSize);
		//4.封装查询结果并返回
		return new PageObject<>(rowCount, records, pageSize, pageCurrent);
	}

}
