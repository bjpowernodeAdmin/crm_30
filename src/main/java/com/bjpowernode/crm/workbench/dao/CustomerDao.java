package com.bjpowernode.crm.workbench.dao;

import java.util.List;

import com.bjpowernode.crm.workbench.domain.Customer;

public interface CustomerDao {

	Customer getByName(String company);

	int save(Customer cus);

	List<String> getCustomerName(String name);

}
