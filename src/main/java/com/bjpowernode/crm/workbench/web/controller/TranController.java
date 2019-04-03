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
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

public class TranController extends HttpServlet{
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("进入到交易模块控制器");
		
		String path = request.getServletPath();
		
		if("/workbench/transaction/getUserList.do".equals(path)){
			
			getUserList(request,response);
			
		}else if("/workbench/transaction/getCustomerName.do".equals(path)){
			
			getCustomerName(request,response);
			
		}else if("/workbench/transaction/save.do".equals(path)){
			
			save(request,response);
			
		}else if("/workbench/transaction/detail.do".equals(path)){
			
			detail(request,response);
			
		}else if("/workbench/transaction/getHistoryListByTranId.do".equals(path)){
			
			getHistoryListByTranId(request,response);
			
		}else if("/workbench/transaction/changeStage.do".equals(path)){
			
			changeStage(request,response);
			
		}else if("/workbench/transaction/getCharts.do".equals(path)){
			
			getCharts(request,response);
			
		}
		
		
	}

	private void getCharts(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  取得统计图表数据  的操作");
		
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		
		/*
		 * 要管业务层要什么？在于前端要什么？
		 * 前端要
		 * 	total:int
		 *  dataList:List<Map<String,Object>>
		 * 		
		 *  业务层就应该为我们返回total和dataList
		 *  	
		 *  怎么返回？
		 *  	使用map或者vo
		 *  
		 * 
		*/
		
		/*
		 * map
		 * 	map.put("total":100);
		 *  map.put("dataList":List<Map>)
		 * 
		 */
		Map<String,Object> map = ts.getCharts();
		
		PrintJson.printJsonObj(response,map);
		
	}

	private void changeStage(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  改变阶段  的操作");
		
		String id = request.getParameter("id");
		String stage = request.getParameter("stage");
		String money = request.getParameter("money");
		String expectedDate = request.getParameter("expectedDate");
		String editTime = DateTimeUtil.getSysTime();
		String editBy = ((User)request.getSession().getAttribute("user")).getName();
		
		Tran t = new Tran();
		t.setId(id);
		t.setStage(stage);
		t.setMoney(money);
		t.setExpectedDate(expectedDate);
		t.setEditBy(editBy);
		t.setEditTime(editTime);
		
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		
		boolean flag = ts.changeStage(t);
		
		Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
		String possibility = pMap.get(stage);
		t.setPossibility(possibility);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success",flag);
		map.put("t",t);
		
		PrintJson.printJsonObj(response,map);
		
	}

	private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  根据交易id取得对应的交易历史列表  的操作");
		
		String tranId = request.getParameter("tranId");
		
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		
		List<TranHistory> thList = ts.getHistoryListByTranId(tranId);
		
		Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
		
		//遍历所有查询出来的历史，通过阶段，处理可能性
		for(TranHistory th : thList){
			
			String stage = th.getStage();
			String possibility = pMap.get(stage);
			
			//将可能性封装到th对象中
			th.setPossibility(possibility);
			
		}
		
		PrintJson.printJsonObj(response, thList);
		
		
	}

	private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("进入到  跳转到详细信息页  的操作");
		
		String id = request.getParameter("id");
		
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		
		Tran t = ts.detail(id);
		
		//处理可能性，将可能性值封装到t的possibility中
		//取得阶段
		String stage = t.getStage();
		//取得阶段和可能性之间的对应关系
		Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
		//通过阶段和可能性之间的对应关系，以阶段为key，取得可能性value值
		String possibility = pMap.get(stage);
		t.setPossibility(possibility);
		
		request.setAttribute("t",t);
		request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
		
		
	}

	private void save(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		System.out.println("进入到  保存交易  的操作");
		
		String id = UUIDUtil.getUUID();
		String owner = request.getParameter("owner");
		String money = request.getParameter("money");
		String name = request.getParameter("name");
		String expectedDate = request.getParameter("expectedDate");
		String customerName = request.getParameter("customerName");	//得到客户名称，但是我们需要保存到表中的是客户的id，这个名称需要在业务层进一步处理
		String stage = request.getParameter("stage");
		String type = request.getParameter("type");
		String source = request.getParameter("source");
		String activityId = request.getParameter("activityId");
		String contactsId = request.getParameter("contactsId");
		String createTime = DateTimeUtil.getSysTime();
		String createBy = ((User)request.getSession().getAttribute("user")).getName();
		String description = request.getParameter("description");
		String contactSummary = request.getParameter("contactSummary");
		String nextContactTime = request.getParameter("nextContactTime");
		
		Tran t = new Tran();
		t.setId(id);
		t.setOwner(owner);
		t.setMoney(money);
		t.setName(name);
		t.setExpectedDate(expectedDate);
		t.setStage(stage);
		t.setType(type);
		t.setSource(source);
		t.setActivityId(activityId);
		t.setContactsId(contactsId);
		t.setCreateBy(createBy);
		t.setCreateTime(createTime);
		t.setDescription(description);
		t.setContactSummary(contactSummary);
		t.setNextContactTime(nextContactTime);
		
		TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
		
		boolean flag = ts.save(t,customerName);
		
		if(flag){
			
			response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
			
		}
		
		
	}

	private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  根据客户名称 模糊查询 客户名称列表  的操作");
		
		String name = request.getParameter("name");
		
		CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
		
		List<String> sList = cs.getCustomerName(name);
		
		PrintJson.printJsonObj(response, sList);
		
	}

	private void getUserList(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		System.out.println("进入到 查询用户信息列表 的操作");
		
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
		
		List<User> uList = us.getUserList();
		
		request.setAttribute("uList",uList);
		request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
		
	}



	
}











































