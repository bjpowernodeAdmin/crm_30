package com.bjpowernode.crm.settings.dao;

import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.settings.domain.User;

public interface UserDao {

	User login(Map<String, String> paramMap);

	List<User> getUserList();

}
