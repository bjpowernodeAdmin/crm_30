package com.bjpowernode.crm.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter{

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		System.out.println("进入到过滤字符编码的过滤器");
		
		//过滤post请求的中文参数
		req.setCharacterEncoding("UTF-8");
		
		//过滤响应流响应中文
		resp.setContentType("text/html;charset=utf-8");
		
		//将请求放行
		chain.doFilter(req, resp);
		
	}

}
