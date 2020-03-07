package com.jt.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jt.pojo.Item;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
@SpringBootTest
public class HttpClientTest {
	
	/**
	 * 1.测试HttpClient中的get请求
	 * 步骤:
	 * 		1.实例化httpClient对象
	 * 		2.确定访问url地址
	 * 		3.封装请求的类型 get/post/put/delete
	 * 		4.发起http请求,获取响应对象 response.
	 * 		5.校验返回对象的状态码信息.  
	 * 			200表示成功
	 * 			400请求的参数异常 
	 * 			404请求路径不匹配
	 * 			406请求的返回值类型异常
	 * 			500后台服务器运行异常
	 * 			504请求超时. 后台服务器宕机/遇忙
	 * 		6.如果状态码信息为200,则动态的获取响应数据.
	 * 		7.获取返回值数据,之后进行业务调用.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * 			
	 */
	
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		HttpClient httpClient = HttpClients.createDefault();
		String url = "https://www.baidu.com";
		HttpGet httpGet = new HttpGet(url);
		HttpPost httpPost = new HttpPost(url);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if(httpResponse.getStatusLine().getStatusCode()==200) {
			//表示用户请求成功!!
			HttpEntity entity = httpResponse.getEntity();  //返回值数据的实体对象
			String json = EntityUtils.toString(entity,"utf-8");
			System.out.println(json);
		}else {
			//一定请求有误.
			System.out.println("服务器正忙,请稍后!!!");
		}
	}
	
	
	/**
	 * 测试工具API是否有效
	 */
	@Autowired
	private HttpClientService httpClientService;
	
	@Test
	public void testParams() {
		String url = "http://www.baidu.com";
		Map<String,String> params = new HashMap<>();
		params.put("id", "101");
		params.put("name", "tomcat");
		String result = httpClientService.doGet(url, params, null);
		System.out.println(result);
	}
	
	
	/**
	 * 测试后台动态获取数据,是否成功!!!
	 * http://manage.jt.com/web/item/findItemById?itemId=562379
	 */
	//远程获取服务器数据,并且转化为自己的对象.
	@Test
	public void testFindItem() {
		String url = "http://manage.jt.com/web/item/findItemById";
		Map<String,String> params = new HashMap<>();
		params.put("itemId", "562379");
		String result = httpClientService.doGet(url, params);
		//将JSON转化为对象
		Item item = ObjectMapperUtil.toObj(result, Item.class);
		System.out.println(item);
	}
	
	
	
	
	
	
	
	
	
}
