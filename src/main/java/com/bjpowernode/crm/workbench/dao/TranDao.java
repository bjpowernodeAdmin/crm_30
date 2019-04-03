package com.bjpowernode.crm.workbench.dao;

import java.util.List;
import java.util.Map;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

public interface TranDao {

	int save(Tran t);

	Tran detail(String id);

	int changeStage(Tran t);

	List<Map<String, Object>> getCharts();

	int getTotal();
	
	
	

}
