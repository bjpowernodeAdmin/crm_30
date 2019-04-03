package com.bjpowernode.crm.workbench.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

public class ClueController extends HttpServlet{
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("进入到线索模块控制器");
		
		String path = request.getServletPath();
		
		if("/workbench/clue/getUserList.do".equals(path)){
			
			getUserList(request,response);
			
		}else if("/workbench/clue/save.do".equals(path)){
			
			save(request,response);
			
		}else if("/workbench/clue/detail.do".equals(path)){
			
			detail(request,response);
			
		}else if("/workbench/clue/getActivityListByClueId.do".equals(path)){
			
			getActivityListByClueId(request,response);
			
		}else if("/workbench/clue/unbund.do".equals(path)){
			
			unbund(request,response);
			
		}else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){
			
			getActivityListByNameAndNotByClueId(request,response);
			
		}else if("/workbench/clue/bund.do".equals(path)){
			
			bund(request,response);
			
		}else if("/workbench/clue/getActivityListByName.do".equals(path)){
			
			getActivityListByName(request,response);
			
		}else if("/workbench/clue/convert.do".equals(path)){
			
			convert(request,response);
			
		}
		
		
	}

	private void convert(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		System.out.println("进入到   执行线索转换   的操作");
		
		String flag = request.getParameter("flag");
		
		String clueId = request.getParameter("clueId");
		
		String createBy = ((User)request.getSession().getAttribute("user")).getName();
		
		Tran t = null;
		
		//如果需要创建交易
		if("a".equals(flag)){
			
			t = new Tran();
			
			//取得交易相关的表单元素，将表单元素值封装到t里面
			String money = request.getParameter("money");
			String name = request.getParameter("name");
			String expectedDate = request.getParameter("expectedDate");
			String stage = request.getParameter("stage");
			String activityId = request.getParameter("activityId");
			String id = UUIDUtil.getUUID();
			String createTime = DateTimeUtil.getSysTime();
			
			t.setId(id);
			t.setMoney(money);
			t.setName(name);
			t.setExpectedDate(expectedDate);
			t.setStage(stage);
			t.setActivityId(activityId);
			t.setCreateBy(createBy);
			t.setCreateTime(createTime);
			
		}
		
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		
		boolean flag1 = cs.convert(clueId,t,createBy);
		
		if(flag1){
			
			response.sendRedirect(request.getContextPath() + "/workbench/clue/index.jsp");
			
		}
		
		
	}

	private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  查询市场活动列表（根据名字模糊查）   的操作");
		
		String aname = request.getParameter("aname");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		List<Activity> aList = as.getActivityListByName(aname);
		
		PrintJson.printJsonObj(response, aList);
		
	}

	private void bund(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  关联市场活动   的操作");
		
		String cid = request.getParameter("cid");
		String aids[] = request.getParameterValues("aid");
		
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		
		boolean flag = cs.bund(cid,aids);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  查询市场活动信息列表（按照名称模糊查询+非关联的线索id）   的操作");
		
		String aname = request.getParameter("aname");
		String clueId = request.getParameter("clueId");
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("aname", aname);
		map.put("clueId", clueId);
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);
		
		PrintJson.printJsonObj(response, aList);
		
		
	}

	private void unbund(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  解除关联   的操作");
		
		//需要解除关联的关联关系表的id
		String id = request.getParameter("id");
		
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		
		boolean flag = cs.unbund(id);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  根据线索id取得关联的市场活动列表   的操作");
		
		String clueId = request.getParameter("clueId");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		List<Activity> aList = as.getActivityListByClueId(clueId);
		
		PrintJson.printJsonObj(response, aList);
		
	}

	private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		System.out.println("进入到  跳转到详细信息页   的操作");
		
		String id = request.getParameter("id");
		
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		
		Clue c = cs.detail(id);
		
		request.setAttribute("c",c);
		request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
		
	}

	private void save(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到   添加线索   的操作");
		
		String id = UUIDUtil.getUUID();
		String fullname = request.getParameter("fullname");
		String appellation = request.getParameter("appellation");
		String owner = request.getParameter("owner");
		String company = request.getParameter("company");
		String job = request.getParameter("job");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String website = request.getParameter("website");
		String mphone = request.getParameter("mphone");
		String state = request.getParameter("state");
		String source = request.getParameter("source");
		String createTime = DateTimeUtil.getSysTime();
		String createBy = ((User)request.getSession().getAttribute("user")).getName();
		String description = request.getParameter("description");
		String contactSummary = request.getParameter("contactSummary");
		String nextContactTime = request.getParameter("nextContactTime");
		String address = request.getParameter("address");
		
		Clue c = new Clue();
		c.setAddress(address);
		c.setAppellation(appellation);
		c.setCompany(company);
		c.setContactSummary(contactSummary);
		c.setCreateBy(createBy);
		c.setCreateTime(createTime);
		c.setDescription(description);
		c.setEmail(email);
		c.setFullname(fullname);
		c.setId(id);
		c.setJob(job);
		c.setMphone(mphone);
		c.setNextContactTime(nextContactTime);
		c.setOwner(owner);
		c.setPhone(phone);
		c.setSource(source);
		c.setState(state);
		c.setWebsite(website);
		
		ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
		
		boolean flag = cs.save(c);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getUserList(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("取得用户信息列表的操作");
		
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
		
		List<User> uList = us.getUserList();
		
		PrintJson.printJsonObj(response, uList);
		
		
	}


	
}











































