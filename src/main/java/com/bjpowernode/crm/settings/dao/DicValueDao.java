package com.bjpowernode.crm.settings.dao;

import java.util.List;

import com.bjpowernode.crm.settings.domain.DicValue;

public interface DicValueDao {

	List<DicValue> getValueListByTypeCode(String code);

}
