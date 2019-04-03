package com.bjpowernode.crm.settings.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

public class UserController extends HttpServlet{
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("进入到用户模块控制器");
		
		String path = request.getServletPath();
		
		if("/settings/user/login.do".equals(path)){
			
			login(request,response);
			
		}else if("/settings/user/xxx.do".equals(path)){
			
			//xxx(request,response);
			
		}
		
		
	}

	private void login(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  验证登录  的操作");
		
		String loginAct = request.getParameter("loginAct");
		String loginPwd = request.getParameter("loginPwd");
		//将密码明文转换为md5密文
		loginPwd = MD5Util.getMD5(loginPwd);
		//取得浏览器端的ip地址
		String ip = request.getRemoteAddr();
		
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
		
		try{
			
			User user = us.login(loginAct,loginPwd,ip);
			
			//在登录成功之后，需要将user对象保存到session域中
			request.getSession().setAttribute("user",user);
			
			//登录成功，为前端提供{"success":true}
			PrintJson.printJsonFlag(response, true);
			
			
			
		}catch(Exception e){
			
			//错误消息
			String msg = e.getMessage();
			
			//登录失败，为前端提供{"success":false,"msg":"?"}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("success", false);
			map.put("msg",msg);
			
			PrintJson.printJsonObj(response,map);
			
			
		}
		
		
		
		
	}
	
}











































