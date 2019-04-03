package com.bjpowernode.crm.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bjpowernode.crm.utils.DateTimeUtil;

public class Test3 {

	public static void main(String[] args) {
		
		//验证锁定状态lockState
		/*
		 * 0：锁定
		 * 1：启用
		 * 
		 */
		
		String lockState = "1";
		if("0".equals(lockState)){
			
			System.out.println("账号处于锁定状态");
			
		}else{
			
			System.out.println("账号处于启用状态");
			
		}
		
		
		
		
		
	}

}
