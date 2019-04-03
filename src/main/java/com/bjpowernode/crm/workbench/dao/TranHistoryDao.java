package com.bjpowernode.crm.workbench.dao;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.TranHistory;

public interface TranHistoryDao {

	int save(TranHistory th);

	List<TranHistory> getHistoryListByTranId(String tranId);

}
