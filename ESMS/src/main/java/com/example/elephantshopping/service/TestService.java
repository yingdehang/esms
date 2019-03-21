package com.example.elephantshopping.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.TestDao;
@Service
public class TestService 
{
	@Autowired
	private TestDao testDao;
	
	
	public List<Map<String, Object>> getAll()
	{
		return testDao.getAll();
	}

}
