package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_user")
@Data
@Accessors(chain=true)
public class User extends BasePojo{
	@TableId(type=IdType.AUTO)
	private Long id;			//用户Id
	private String username;	//用户名
	private String password;	//MD5加密
	private String phone;		//电话号码
	private String email;		//邮箱
	
}
