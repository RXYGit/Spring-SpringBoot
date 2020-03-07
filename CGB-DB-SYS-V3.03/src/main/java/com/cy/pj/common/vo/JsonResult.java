package com.cy.pj.common.vo;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 基于此对象封装控制层要响应到客户端的数据
 */
@Data
@NoArgsConstructor
public class JsonResult implements Serializable{
	private static final long serialVersionUID = 677048178703375661L;
	/**状态码*/
	private int state=1;//1 表示ok,0标识错误
	/**状态信息*/
	private String message="ok";//状态信息
	/**借助此属性封装控制层从业务获得数据*/
	private Object data;
	public JsonResult(String message) {
		this.message=message;
	}
	public JsonResult(Object data) {
		this.data=data;
	}
	public JsonResult(Throwable e) {
		this.state=0;
		this.message=e.getMessage();
	}	
}








