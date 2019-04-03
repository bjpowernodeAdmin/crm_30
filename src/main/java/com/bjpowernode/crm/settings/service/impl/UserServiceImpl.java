package com.bjpowernode.crm.settings.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

	public User login(String loginAct, String loginPwd, String ip) throws LoginException {
		
		/*
		 * 执行登录操作
		 * 
		 * 验证账号密码是否正确
		 * 	select * from tbl_user where loginAct=#{loginAct} and loginPwd=#{loginPwd}
		 * 通过执行以上sql语句为我们返回一个user对象
		 * 
		 * 判断user对象
		 * 如果user对象为null，说明账号密码错误，抛出自定义异常，异常消息为：账号密码错误
		 * 
		 * 			 
		 * 如果user对象不为null，说明账号密码正确
		 * 从user对象中取得 expireTime，lockState，allowIps继续判断
		 * 
		 * 如果账号失效，抛出自定义异常，异常消息为：账号已失效
		 * 如果账号锁定，抛出自定义异常，异常消息为：账号已锁定
		 * 如果ip地址不正确，抛出自定义异常，异常消息为：ip地址受限
		 * 
		 * 
		 * 
		 */
		
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("loginAct", loginAct);
		paramMap.put("loginPwd", loginPwd);
		
		User user = userDao.login(paramMap);
		
		//如果user对象为null，说明账号密码错误，抛出自定义异常，异常消息为：账号密码错误
		if(user==null){
			
			throw new LoginException("账号密码错误");
			
		}
		
		//如果程序能够顺利的走到该行，说明账号密码是正确的
		/*
		 * 如果user对象不为null，说明账号密码正确
		 * 从user对象中取得 expireTime，lockState，allowIps继续判断
		 */
		String expireTime = user.getExpireTime();
		String currentTime = DateTimeUtil.getSysTime();
		if(expireTime.compareTo(currentTime)<0){
			
			throw new LoginException("账号已失效");
			
		}
		
		String lockState = user.getLockState();
		if("0".equals(lockState)){
			
			throw new LoginException("账号已锁定");
			
		}
		
		String allowIps = user.getAllowIps();
		if(!allowIps.contains(ip)){
			
			throw new LoginException("ip地址受限");
			
		}
		
		
		
		return user;
	}

	public List<User> getUserList() {
		
		List<User> uList = userDao.getUserList();
		
		return uList;
	}

	
	
}







































