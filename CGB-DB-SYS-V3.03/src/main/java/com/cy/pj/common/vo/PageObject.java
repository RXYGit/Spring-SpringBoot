package com.cy.pj.common.vo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * VO:(Value Object)
 * 借助此类封装从数据层获取的当前页记录以及
 * 总记录数等相关分页信息
 */
@Data
@NoArgsConstructor
public class PageObject<T> implements Serializable{//泛型类
	  private static final long serialVersionUID = 1684876792546164773L;
	  /**总行数*/
	  private Integer rowCount=0;
	  /**当前页要呈现的记录*/
	  private List<T> records;
	  /**总页数*/
	  private Integer pageCount=0;
	  /**页面大小(每页要呈现的记录数)*/
	  private Integer pageSize=3;
	  /**当前页页码值*/
	  private Integer pageCurrent=1;
	  public PageObject(Integer rowCount, List<T> records, Integer pageSize, Integer pageCurrent) {
		super();
		this.rowCount = rowCount;
		this.records = records;
		this.pageSize = pageSize;
		this.pageCurrent = pageCurrent;
		//计算分页的小算法
//		this.pageCount=rowCount/pageSize;
//		if(rowCount%pageSize!=0) {
//			this.pageCount++;
//		}
		//计算分页的小算法(很多方法)
		this.pageCount=(rowCount-1)/pageSize+1;
	  }
}





