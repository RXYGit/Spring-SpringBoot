package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.EasyUIImage;

@RestController //JSON数据
public class FileController {
	
	
	/**
	 * 知识点:
	 * 1.<input name="fileImage" type="file" /> 名称一致
	 * 2.文件上传需要添加文件上传视图解析器. 该解析器由SpringMVC负责管理.
	 * 3.编辑参数接收类型. MultipartFile
	 * 4.最大上传1M图片
	 * @param fileImage
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException   
	 */
	@RequestMapping("/file")
	public String file(MultipartFile fileImage) throws IllegalStateException, IOException {
		//1.获取图片的名称
		String name = fileImage.getOriginalFilename();
		//2.准备文件上传路径  D:xxxx
		File dirFile = new File("E:\\JT_IMAGE");
		//2.1判断文件是否存在
		if(!dirFile.exists()) {
			//如果文件不存在的话,则创建目录.
			dirFile.mkdirs();
		}
		//3.实现文件上传 E:/JT_IMAGE/A.JPG
		String filePath = "E:/JT_IMAGE/"+name;
		fileImage.transferTo(new File(filePath));
		return "调用成功!!!!!";
	}
	
	
	@Autowired
	private FileService fileService;
	/**
	 * url:http://localhost:8091/pic/upload?dir=image 图片JS生效
	 *   参数:uploadFile
	 *   返回值: EasyUIImage VO对象
	 */
	@RequestMapping("/pic/upload")
	public EasyUIImage uploadFile(MultipartFile uploadFile) {
		
		return fileService.uploadFile(uploadFile);
	}
	
	
	
	
	
	
	
}
