package com.bjpowernode.crm.web.listener;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

public class SysInitListener implements ServletContextListener{
	
	
	/*
	 * 该方法是用来监听上下文域对象创建的方法
	 * 如果上下文域对象创建了，则马上执行该方法
	 * 
	 * 参数event：可以使用该参数来取得我们监听的对象，你监听的是哪个对象，就可以通过event来取得哪个对象
	 * 		例如，我们现在要监听的是上下文对象，使用event就可以取得上下文对象
	 * 
	 */
	public void contextInitialized(ServletContextEvent event) {
		
		ServletContext application = event.getServletContext();
		
		//处理数据字典---------------------------------------------------------------------------
		
		//System.out.println("上下文域对象创建了,我也拿到了这个域对象："+application);
		
		System.out.println("处理数据字典开始------------------------------------------");
		
		DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());	
		
		Map<String,List<DicValue>> map = ds.getAll();
		
		//将map中的键值对解析为application保存的键值对
		Set<String> set = map.keySet();
		for(String key:set){
			
			application.setAttribute(key, map.get(key));
			
		}
		
		System.out.println("处理数据字典结束------------------------------------------");
		
		
		//处理阶段和可能性的对应关系---------------------------------------------------------------------------
		/*
		 * 1.解析properties文件
		 * 2.将properties文件中的键值对保存到map中
		 * 3.将map保存到application中
		 */
		
		Map<String,String> pMap = new HashMap<String,String>();
		
		ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
		
		Enumeration<String> e = rb.getKeys();
		
		while(e.hasMoreElements()){
			
			String stage = e.nextElement();
			String possibility = rb.getString(stage);
			
			pMap.put(stage, possibility);
			
		}
		
		application.setAttribute("pMap",pMap);
		
	}
	
}
















































