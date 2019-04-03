package com.bjpowernode.crm.test;

import org.junit.Test;

import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

import junit.framework.Assert;

public class ActivityTest {
	
	@Test
	public void testSave(){
		
		Activity a = new Activity();
		a.setId(UUIDUtil.getUUID());
		a.setName("宣传推广会123");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.save(a);
		
		//断言
		Assert.assertEquals(flag,true);
		
	}
	
	@Test
	public void testUpdate(){
		
		Activity a = new Activity();
		a.setId("49d093ea442543a484ee92caf03b3ca9");
		a.setName("发传单1a");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.update(a);
		
		//断言
		Assert.assertEquals(flag,true);
		
		
		
	}
	
	
	
	
}
