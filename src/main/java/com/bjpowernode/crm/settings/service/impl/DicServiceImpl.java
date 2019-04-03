package com.bjpowernode.crm.settings.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

public class DicServiceImpl implements DicService {
	
	private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
	private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
	public Map<String, List<DicValue>> getAll() {
		
		Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();
		
		//取出所有的字典类型
		List<DicType> dtList = dicTypeDao.getTypeList();
		
		//遍历所有的类型， 根据每一种类型编码来取得对应的值的列表，将值的列表以对应类型的方式保存到map中
		for(DicType dt : dtList){
			
			//根据每一种类型对象取得类型编码
			String code = dt.getCode();
			
			//根据每一种类型编码来取得对应的值的列表
			List<DicValue> dvList = dicValueDao.getValueListByTypeCode(code);
			
			map.put(code+"List", dvList);
			
		}
		
		
		
		//将map返回
		return map;
	}
	
	
	
}
