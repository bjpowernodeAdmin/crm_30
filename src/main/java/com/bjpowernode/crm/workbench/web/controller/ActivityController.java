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
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

public class ActivityController extends HttpServlet{
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("进入到市场活动模块控制器");
		
		String path = request.getServletPath();
		
		if("/workbench/activity/getUserList.do".equals(path)){
			
			getUserList(request,response);
			
		}else if("/workbench/activity/save.do".equals(path)){
			
			save(request,response);
			
		}else if("/workbench/activity/pageList.do".equals(path)){
			
			pageList(request,response);
			
		}else if("/workbench/activity/delete.do".equals(path)){
			
			delete(request,response);
			
		}else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
			
			getUserListAndActivity(request,response);
			
		}else if("/workbench/activity/update.do".equals(path)){
			
			update(request,response);
			
		}else if("/workbench/activity/detail.do".equals(path)){
			
			detail(request,response);
			
		}else if("/workbench/activity/getRemarkListByAid.do".equals(path)){
			
			getRemarkListByAid(request,response);
			
		}else if("/workbench/activity/deleteRemark.do".equals(path)){
			
			deleteRemark(request,response);
			
		}else if("/workbench/activity/saveRemark.do".equals(path)){
			
			saveRemark(request,response);
			
		}else if("/workbench/activity/updateRemark.do".equals(path)){
			
			updateRemark(request,response);
			
		}
		
		
	}

	private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 修改备注   操作");
		
		String id = request.getParameter("id");
		String noteContent = request.getParameter("noteContent");
		String editTime = DateTimeUtil.getSysTime();
		String editBy = ((User)request.getSession().getAttribute("user")).getName();
		String editFlag = "1";
		
		ActivityRemark ar = new ActivityRemark();
		ar.setId(id);
		ar.setNoteContent(noteContent);
		ar.setEditBy(editBy);
		ar.setEditTime(editTime);
		ar.setEditFlag(editFlag);
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.updateRemark(ar);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("ar", ar);
		
		PrintJson.printJsonObj(response,map);
		
	}

	private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  添加备注   操作");
		
		String id = UUIDUtil.getUUID();
		String noteContent = request.getParameter("noteContent");
		String activityId = request.getParameter("activityId");
		String createTime = DateTimeUtil.getSysTime();
		String createBy = ((User)request.getSession().getAttribute("user")).getName();
		String editFlag = "0";
		
		ActivityRemark ar = new ActivityRemark();
		ar.setId(id);
		ar.setNoteContent(noteContent);
		ar.setActivityId(activityId);
		ar.setCreateBy(createBy);
		ar.setCreateTime(createTime);
		ar.setEditFlag(editFlag);
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.saveRemark(ar);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", flag);
		map.put("ar", ar);
		
		PrintJson.printJsonObj(response,map);
		
		
	}

	private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  删除备注   操作");
		
		String id = request.getParameter("id");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.deleteRemark(id);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  根据市场活动id取得备注信息列表   操作");
		
		String aid = request.getParameter("aid");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		List<ActivityRemark> arList = as.getRemarkListByAid(aid);
		
		PrintJson.printJsonObj(response, arList);
		
	}

	private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("进入到   跳转到详细信息页   操作");
		
		String id = request.getParameter("id");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		Activity a = as.detail(id);
		
		request.setAttribute("a",a);
		request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);
		
		
	}

	private void update(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 执行市场活动修改  操作");
		
		String id = request.getParameter("id");
		String owner = request.getParameter("owner");
		String name = request.getParameter("name");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String cost = request.getParameter("cost");
		String description = request.getParameter("description");
		//修改时间：当前系统时间
		String editTime = DateTimeUtil.getSysTime();
		//修改人：当前登录用户姓名
		String editBy = ((User)request.getSession().getAttribute("user")).getName();
		
		Activity a = new Activity();
		a.setId(id);
		a.setOwner(owner);
		a.setName(name);
		a.setStartDate(startDate);
		a.setEndDate(endDate);
		a.setCost(cost);
		a.setDescription(description);
		a.setEditBy(editBy);
		a.setEditTime(editTime);
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.update(a);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  查询用户信息列表和市场活动单条记录   操作");
		
		String id = request.getParameter("id");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		Map<String,Object> map = as.getUserListAndActivity(id);
		
		PrintJson.printJsonObj(response, map);
		
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到  删除市场活动   操作");
		
		String ids[] = request.getParameterValues("id");
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.delete(ids);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void pageList(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到   查询市场活动信息列表（分页查询+条件查询）   操作");
		
		String name = request.getParameter("name");
		String owner = request.getParameter("owner");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String pageNoStr = request.getParameter("pageNo");
		String pageSizeStr = request.getParameter("pageSize");
		int pageNo = Integer.valueOf(pageNoStr);
		int pageSize = Integer.valueOf(pageSizeStr);
		//分页查询需要的是
		/*
		 * skipCount:略过的记录数   没有 需要计算出来
		 * pageSize：每页展现多少条记录  有
		 */
		int skipCount = (pageNo-1)*pageSize;
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		/*
		 * 
		 * 参数：name owner startDate endDate skipCount pageSize
		 * 将以上参数保存到map中管理，传递map参数即可
		 * 
		 * 
		 * 返回值：需要业务层为我们提供？
		 * 			total   dataList
		 * 		
		 *  	业务层需要为我们返回以上两组数据
		 *  	这两组数据应该以什么方式返回？
		 *  	（1）使用map将以上两组数据保存，返回map,就相当于返回了以上两组数据
		 *  	
		 *  	（2）如果这两组数据的组合，来未来的复用性很高，每一次返回map比较麻烦，所以我们可以为这两组数据创建一个vo
		 *  		使用vo封装以上两组数据，返回vo，就相当于返回了以上两组数据
		 *  	
		 * 
		 * 
		 * 
		 */
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name",name);
		map.put("owner",owner);
		map.put("startDate",startDate);
		map.put("endDate",endDate);
		map.put("skipCount",skipCount);
		map.put("pageSize",pageSize);
		
		PaginationVO<Activity> vo = as.pageList(map);
		
		PrintJson.printJsonObj(response, vo);
		
		
	}

	private void save(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 执行市场活动添加  操作");
		
		String id = UUIDUtil.getUUID();
		String owner = request.getParameter("owner");
		String name = request.getParameter("name");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String cost = request.getParameter("cost");
		String description = request.getParameter("description");
		//创建时间：当前系统时间
		String createTime = DateTimeUtil.getSysTime();
		//创建人：当前登录用户姓名
		String createBy = ((User)request.getSession().getAttribute("user")).getName();
		
		Activity a = new Activity();
		a.setId(id);
		a.setOwner(owner);
		a.setName(name);
		a.setStartDate(startDate);
		a.setEndDate(endDate);
		a.setCost(cost);
		a.setDescription(description);
		a.setCreateBy(createBy);
		a.setCreateTime(createTime);
		
		ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
		
		boolean flag = as.save(a);
		
		PrintJson.printJsonFlag(response, flag);
		
	}

	private void getUserList(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("进入到 取得用户信息列表  的操作");
		
		UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
		
		List<User> uList = us.getUserList();
		
		PrintJson.printJsonObj(response, uList);
		
		
	}

	
}











































