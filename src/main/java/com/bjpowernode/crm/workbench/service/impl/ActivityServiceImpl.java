package com.bjpowernode.crm.workbench.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
	
	private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
	private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
	private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
	public boolean save(Activity a) {
		
		int count = activityDao.save(a);
		
		boolean flag = true;
		
		if(count!=1){
			
			flag = false;
			
		}
		
		
		return flag;
	}
	public PaginationVO<Activity> pageList(Map<String, Object> map) {
		
		//取得total
		int total = activityDao.getTotalByCondition(map);
		
		//取得dataList
		List<Activity> dataList = activityDao.getActivityListByCondition(map);
		
		
		//将total和dataList封装到vo中
		PaginationVO<Activity> vo = new PaginationVO<Activity>();
		vo.setTotal(total);
		vo.setDataList(dataList);
		
		
		//将vo返回
		return vo;
	}
	public boolean delete(String[] ids) {
		
		//删除市场活动的关联删除市场活动的备注
		//想：就相当于是我们现在要删除班级，还需要删除班级关联的所有的学生
		
		boolean flag = true;
		
		//查询出市场活动的id数组关联的所有的备注的数量（应该删除的数量）
		int total = activityRemarkDao.getTotalByAids(ids);
		
		//删除关联的备注的数量（实际删除的数量）
		int count1 = activityRemarkDao.deleteByAids(ids);
		
		//如果实际删除的备注的数量和应该删除的备注的数量相同，则证明备注删除成功
		if(total!=count1){
			
			flag = false;
			
		}
		
		//删除市场活动
		int count2 = activityDao.delete(ids);
		if(count2!=ids.length){
			
			flag = false;
		
		}
		
		return flag;
	}
	public Map<String, Object> getUserListAndActivity(String id) {
		
		//取得uList
		List<User> uList = userDao.getUserList();
		
		//取得Activity a
		Activity a = activityDao.getById(id);
		
		//将uList和a保存到map中
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uList",uList);
		map.put("a",a);
		
		//返回map
		return map;
	}
	public boolean update(Activity a) {
		
		int count = activityDao.update(a);
		
		boolean flag = true;
		
		if(count!=1){
			
			flag = false;
			
		}
		
		
		return flag;
	}
	public Activity detail(String id) {
		
		Activity a = activityDao.detail(id);
		
		return a;
	}
	public List<ActivityRemark> getRemarkListByAid(String aid) {
		
		List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(aid);
		
		return arList;
	}
	public boolean deleteRemark(String id) {
		
		int count = activityRemarkDao.deleteRemark(id);
		
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}
	public boolean saveRemark(ActivityRemark ar) {
		
		int count = activityRemarkDao.saveRemark(ar);
		
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}
	public boolean updateRemark(ActivityRemark ar) {
		
		int count = activityRemarkDao.updateRemark(ar);
		
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}
	public List<Activity> getActivityListByClueId(String clueId) {
		
		List<Activity> aList = activityDao.getActivityListByClueId(clueId);
		
		return aList;
	}
	public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
		
		List<Activity> aList = activityDao.getActivityListByNameAndNotByClueId(map);
		
		return aList;
	}
	public List<Activity> getActivityListByName(String aname) {
		
		List<Activity> aList = activityDao.getActivityListByName(aname);
		
		return aList;
	}
	
}





















































