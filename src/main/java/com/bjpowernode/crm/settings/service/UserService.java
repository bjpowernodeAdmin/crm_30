package com.bjpowernode.crm.settings.service;

import java.util.List;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;

public interface UserService {

	User login(String loginAct, String loginPwd, String ip)throws LoginException;

	List<User> getUserList();
	

}
