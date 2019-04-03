package com.bjpowernode.crm.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bjpowernode.crm.settings.domain.User;

public class LoginFilter implements Filter{

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		System.out.println("进入到判断有没有登录过的过滤器");
		
		/*
		 * 如何判断有没有登录过
		 * 
		 * 从request中取sesion
		 * 从session中取user
		 * 
		 * 判断user对象，如果user对象不为null，说明登录过，将请求放行到目标资源
		 * 			     如果user对象为null，说明没有登录过，重定向到登录页
		 * 
		 * 
		 * ServletRequest:父亲
		 * HttpServletRequest：儿子
		 * 
		 * 我们现在有的是父亲，但是我们要用的是儿子
		 * 
		 */
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		
		//如果请求时登录页，或者是登录操作，不需要验证是否登录过，应该自动放行
		String path = request.getServletPath();
		if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
			
			chain.doFilter(req, resp);
		
			
		//除了登录之外的其他请求，需要正常验证有没有登录过	
		}else{
			
			
			User user = (User) request.getSession().getAttribute("user");
			
			//登录过
			if(user!=null){
				
				//将请求放行到目标资源
				chain.doFilter(req, resp);
				
			//没登录过	
			}else{
				
				//重定向到登录页
				response.sendRedirect(request.getContextPath() + "/login.jsp");
				
				
			}
			
			
			
		}
		
		
		
		
		
		
		
		
	}

}
