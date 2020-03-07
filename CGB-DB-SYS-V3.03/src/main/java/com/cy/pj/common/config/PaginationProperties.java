package com.cy.pj.common.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ConfigurationProperties(prefix = "page.config")//告诉springboot读配置文件中指定前缀的内容
@Configuration //描述此类为配置类,交给spring管理
public class PaginationProperties {
	private Integer pageSize=10;//set方法必须有
	
	public Integer getStartIndex(Integer pageCurrent) {
		return (pageCurrent-1)*pageSize;
	}
	//.....
}
