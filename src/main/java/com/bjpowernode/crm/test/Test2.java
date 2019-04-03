package com.bjpowernode.crm.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bjpowernode.crm.utils.DateTimeUtil;

public class Test2 {

	public static void main(String[] args) {
		
		//验证失效时间
		
		//失效时间
		String expireTime = "2019-03-26 10:10:10";
		
		//当前系统时间
		String currentTime = DateTimeUtil.getSysTime();
		
		int count = expireTime.compareTo(currentTime);
		
		if(count<0){
			
			System.out.println("账号过期了");
			
		}else{
			
			System.out.println("账号没过期");
			
		}
		
		
		
	}

}
