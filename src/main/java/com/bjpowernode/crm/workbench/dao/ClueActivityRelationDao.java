package com.bjpowernode.crm.workbench.dao;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {

	int unbund(String id);

	int bund(ClueActivityRelation car);

	List<ClueActivityRelation> getListByCid(String clueId);

	int delete(ClueActivityRelation clueActivityRelation);

	

}
