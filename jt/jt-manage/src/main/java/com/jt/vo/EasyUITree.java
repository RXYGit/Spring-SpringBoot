package com.jt.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor		
public class EasyUITree {
	//[{"id":"3","text":"吃鸡游戏","state":"open/closed"}]
	private Long id;	//树形结构 id唯一标识
	private String text;	//节点的文本信息
	private String state;	//节点状态信息  open打开 closed关闭
}	





