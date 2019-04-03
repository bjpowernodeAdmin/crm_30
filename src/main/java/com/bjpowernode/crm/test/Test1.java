package com.bjpowernode.crm.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bjpowernode.crm.utils.DateTimeUtil;

public class Test1 {

	public static void main(String[] args) {
		
		Date date = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String str = sdf.format(date);
		
		System.out.println(str);
		
	}

}
