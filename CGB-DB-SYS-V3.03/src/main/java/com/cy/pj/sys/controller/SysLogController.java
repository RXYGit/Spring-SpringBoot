package com.cy.pj.sys.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

import lombok.Data;

//@ResponseBody
//@Controller
@RestController //==@Controller+@ReponseBody
@RequestMapping("/log/")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 基于客户端提交的记录id,执行删除业务,并反馈结果
	 * @param ids
	 * @return
	 */
	@RequestMapping("doDeleteObjects")
	//@ResponseBody
	public JsonResult doDeleteObjects(Integer...ids) {
		sysLogService.deleteObjects(ids);
		return new JsonResult("delete ok");
	}
	/**
	 * 基于条件查询当前页要呈现的记录,并将记录封装到JsonResult,目的时
	 * 服务端返回的客户端的信息一个状态.客户端可以基于此状态进行响应数据的处理.
	 * @param username 用户名,与客户端传递参数名相同
	 * @param pageCurrent 当前页码值
	 * @return 封装了业务结果的一个状态对象.
	 */
	@RequestMapping("doFindPageObjects")
	//@ResponseBody
	public JsonResult doFindPageObjects(String username,
			Integer pageCurrent) {
		//JsonResult r=new JsonResult();
		//r.setData(sysLogService.findPageObjects(username, pageCurrent));
		PageObject<SysLog> pageObject=
	    sysLogService.findPageObjects(username, pageCurrent);
		
		return new JsonResult(pageObject);//result.data.records
	}
}






