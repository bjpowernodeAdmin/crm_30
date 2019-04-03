package com.bjpowernode.crm.workbench.service.impl;

import java.util.List;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.ClueActivityRelationDao;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.dao.ClueRemarkDao;
import com.bjpowernode.crm.workbench.dao.ContactsActivityRelationDao;
import com.bjpowernode.crm.workbench.dao.ContactsDao;
import com.bjpowernode.crm.workbench.dao.ContactsRemarkDao;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.CustomerRemarkDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.ContactsActivityRelation;
import com.bjpowernode.crm.workbench.domain.ContactsRemark;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {
	
	private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
	private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
	private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
	
	private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
	private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
	
	private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
	private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
	private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
	
	private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
	private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
	
	
	
	
	public boolean save(Clue c) {
		
		int count = clueDao.save(c);
		
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}

	public Clue detail(String id) {
		
		Clue c = clueDao.detail(id);
		
		return c;
	}

	public boolean unbund(String id) {
		
		int count = clueActivityRelationDao.unbund(id);
		boolean flag = true;
		if(count!=1){
			flag = false;
		}
		
		return flag;
	}

	public boolean bund(String cid, String[] aids) {
		
		boolean flag = true;
		
		for(String aid : aids){
			
			ClueActivityRelation car = new ClueActivityRelation();
			car.setId(UUIDUtil.getUUID());
			car.setActivityId(aid);
			car.setClueId(cid);
			
			int count = clueActivityRelationDao.bund(car);
			
			if(count!=1){
				flag = false;
			}
			
		}
		
		return flag;
	}

	public boolean convert(String clueId, Tran t, String createBy) {
		
		boolean flag = true;
		
		String createTime = DateTimeUtil.getSysTime();
		
		//(1)获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
		//将来从c中取得客户和联系人的基本信息
		Clue c = clueDao.getById(clueId);
		
		//(2)通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
		
		//取得公司名称
		String company = c.getCompany();
		//根据公司名称，到客户表中查询，判断客户是否存在
		//注意：我们要查询的不是数量，而是查询出来客户的对象，如果客户的对象有，那么以下转换流程会使用到该客户的基本信息
		Customer cus = customerDao.getByName(company);
		//如果客户不存在，需要新建一个客户
		if(cus==null){
			
			cus = new Customer();
			cus.setId(UUIDUtil.getUUID());
			cus.setAddress(c.getAddress());
			cus.setContactSummary(c.getContactSummary());
			cus.setCreateBy(createBy);
			cus.setCreateTime(createTime);
			cus.setDescription(c.getDescription());
			cus.setName(company);
			cus.setNextContactTime(c.getNextContactTime());
			cus.setOwner(c.getOwner());
			cus.setPhone(c.getPhone());
			cus.setWebsite(c.getWebsite());
			//添加客户
			int count1 = customerDao.save(cus);
			if(count1!=1){
				flag = false;
			}
			
		}
		
		//(3) 通过线索对象提取联系人信息，保存联系人
		Contacts con = new Contacts();
		con.setId(UUIDUtil.getUUID());
		con.setAddress(c.getAddress());
		con.setAppellation(c.getAppellation());
		con.setContactSummary(c.getContactSummary());
		con.setCreateBy(createBy);
		con.setCreateTime(createTime);
		con.setCustomerId(cus.getId());
		con.setDescription(c.getDescription());
		con.setEmail(c.getEmail());
		con.setFullname(c.getFullname());
		con.setJob(c.getJob());
		con.setMphone(c.getMphone());
		con.setNextContactTime(c.getNextContactTime());
		con.setOwner(c.getOwner());
		con.setSource(c.getSource());
		//添加联系人
		int count2 = contactsDao.save(con);
		if(count2!=1){
			flag = false;
		}
		
		//(4)线索备注转换到客户备注以及联系人备注
		//取得该条线索关联的备注列表
		List<ClueRemark> clueRemarkList = clueRemarkDao.getListByCid(clueId);
		//遍历每一个与该线索关联的备注，将这些备注信息，转换为客户备注和联系人备注
		for(ClueRemark clueRemark : clueRemarkList){
			
			CustomerRemark customerRemark = new CustomerRemark();
			customerRemark.setId(UUIDUtil.getUUID());
			customerRemark.setCreateBy(createBy);
			customerRemark.setCreateTime(createTime);
			customerRemark.setCustomerId(cus.getId());
			customerRemark.setEditFlag("0");
			customerRemark.setNoteContent(clueRemark.getNoteContent());
			//添加客户备注
			int count3 = customerRemarkDao.save(customerRemark);
			if(count3!=1){
				flag = false;
			}
			
			ContactsRemark contactsRemark = new ContactsRemark();
			contactsRemark.setId(UUIDUtil.getUUID());
			contactsRemark.setCreateBy(createBy);
			contactsRemark.setCreateTime(createTime);
			contactsRemark.setContactsId(con.getId());
			contactsRemark.setEditFlag("0");
			contactsRemark.setNoteContent(clueRemark.getNoteContent());
			//添加联系人备注
			int count4 = contactsRemarkDao.save(contactsRemark);
			if(count4!=1){
				flag = false;
			}
			
		}
		
		//(5)“线索和市场活动”的关系转换到“联系人和市场活动”的关系
		//查询出与该条线索关联的 市场活动 列表（以关联关系表的形式查询）
		List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByCid(clueId);
		//遍历出来每一个关联关系对象，取得市场活动id，与联系人id做关联
		for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
			
			ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
			contactsActivityRelation.setId(UUIDUtil.getUUID());
			contactsActivityRelation.setContactsId(con.getId());
			contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
			//添加 联系人和市场活动的关联关系
			int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
			if(count5!=1){
				flag = false;
			}
		}
		
		//(6)如果有创建交易需求，创建一条交易
		//判断t对象，如果t对象不为null，说明需要创建交易
		if(t!=null){
			
			//创建交易
			int count6 = tranDao.save(t);
			if(count6!=1){
				flag = false;
			}
			
			//(7) 如果创建了交易，则创建一条该交易下的交易历史
			//注意：创建交易历史的前提是已经创建完了交易，创建交易历史必须要写在t!=null的if判断里面
			TranHistory th = new TranHistory();
			th.setId(UUIDUtil.getUUID());
			th.setCreateBy(createBy);
			th.setCreateTime(createTime);
			th.setExpectedDate(t.getExpectedDate());
			th.setMoney(t.getMoney());
			th.setStage(t.getStage());
			th.setTranId(t.getId());
			//添加交易历史
			int count7 = tranHistoryDao.save(th);
			if(count7!=1){
				flag = false;
			}
			
		}
		
		//(8)删除线索备注
		for(ClueRemark clueRemark : clueRemarkList){
			
			int count8 = clueRemarkDao.delete(clueRemark);
			if(count8!=1){
				flag = false;
			}
			
		}
		
		//(9)删除线索和市场活动的关系
		for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
			
			int count9 = clueActivityRelationDao.delete(clueActivityRelation);
			if(count9!=1){
				flag = false;
			}
			
		}
		
		//(10)删除线索
		int count10 = clueDao.delete(clueId);
		if(count10!=1){
			flag = false;
		}
		
		
		return flag;
	}

	
	
}











































































