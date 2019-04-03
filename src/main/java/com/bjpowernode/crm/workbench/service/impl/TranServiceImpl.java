package com.bjpowernode.crm.workbench.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

public class TranServiceImpl implements TranService {
	
	private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
	private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
	private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
	public boolean save(Tran t, String customerName) {
		
		boolean flag = true;
		
		/*
		 * 处理客户：
		 * 		根据客户名称查询有没有这个客户，如果有，则将该客户查询出来取其id，赋予到t对象中
		 * 							 如果没有，则创建该客户，将创建完的客户的id，赋予到t对象中
		 */
		Customer cus = customerDao.getByName(customerName);
		if(cus==null){
			
			cus = new Customer();
			cus.setId(UUIDUtil.getUUID());
			cus.setCreateBy(t.getCreateBy());
			cus.setCreateTime(DateTimeUtil.getSysTime());
			cus.setName(customerName);
			
			//添加客户
			int count1 = customerDao.save(cus);
			if(count1!=1){
				flag = false;
			}
			
		}
		
		
		//将客户的id封装到t中，添加交易
		t.setCustomerId(cus.getId());
		int count2 = tranDao.save(t);
		if(count2!=1){
			flag = false;
		}
		
		
		//添加交易历史
		TranHistory th = new TranHistory();
		th.setId(UUIDUtil.getUUID());
		th.setCreateBy(t.getCreateBy());
		th.setCreateTime(DateTimeUtil.getSysTime());
		th.setExpectedDate(t.getExpectedDate());
		th.setMoney(t.getMoney());
		th.setStage(t.getStage());
		th.setTranId(t.getId());
		//添加交易历史
		int count3 = tranHistoryDao.save(th);
		if(count3!=1){
			flag = false;
		}
		
		
		return flag;
	}
	public Tran detail(String id) {
		
		Tran t = tranDao.detail(id);
		
		return t;
	}
	public List<TranHistory> getHistoryListByTranId(String tranId) {
		
		List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);
		
		return thList;
	}
	public boolean changeStage(Tran t) {
		
		boolean flag = true;
		
		//改变阶段
		int count1 = tranDao.changeStage(t);
		if(count1!=1){
			flag = false;
		}
		
		//改变阶段后需要创建一条交易历史
		TranHistory th = new TranHistory();
		th.setId(UUIDUtil.getUUID());
		th.setCreateBy(t.getEditBy());
		th.setCreateTime(DateTimeUtil.getSysTime());
		th.setExpectedDate(t.getExpectedDate());
		th.setMoney(t.getMoney());
		th.setStage(t.getStage());
		th.setTranId(t.getId());
		//添加交易历史
		int count2 = tranHistoryDao.save(th);
		if(count2!=1){
			flag = false;
		}
		
		return flag;
	}
	public Map<String, Object> getCharts() {
		
		int total = tranDao.getTotal();
		
		List<Map<String,Object>> dataList = tranDao.getCharts();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total",total);
		map.put("dataList",dataList);
		
		return map;
	}
	
}





















