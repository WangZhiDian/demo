package com.demo.bussiness.mybatisLocal.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.bussiness.mybatisLocal.dao.SuitMapper;

@Service
public class MybatisService
{
	Logger logger = LoggerFactory.getLogger(MybatisService.class);
	
	@Resource
	SuitMapper suitMapper;
	
	public String getSuitNameById(String suitId)
	{
		logger.info(suitId);
		String suitName = suitMapper.getSuitName();
		logger.info("name:" + suitName);
		return suitName;
	}
	
	
}
