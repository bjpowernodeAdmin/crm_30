package com.bjpowernode.crm.vo;

import java.util.List;

/*
 * 用来处理分页查询的vo
 */
public class PaginationVO<T> {

	private int total;	//查询列表的总条数
	
	private List<T> dataList;	//查询的数据列表

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	
	
	
	
}
