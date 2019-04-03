package com.bjpowernode.crm.workbench.dao;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

public interface ActivityRemarkDao {

	int getTotalByAids(String[] ids);

	int deleteByAids(String[] ids);

	List<ActivityRemark> getRemarkListByAid(String aid);

	int deleteRemark(String id);

	int saveRemark(ActivityRemark ar);

	int updateRemark(ActivityRemark ar);

}
