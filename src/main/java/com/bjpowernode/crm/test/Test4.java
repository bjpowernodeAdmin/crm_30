package com.bjpowernode.crm.test;

import com.bjpowernode.crm.utils.MD5Util;

public class Test4 {
	
	public static void main(String[] args) {
		
		String str = "bjpowernodeTest@163.com";
		
		str = MD5Util.getMD5(str);
		
		System.out.println(str);
		
	}
	
}
