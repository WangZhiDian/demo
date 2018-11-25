package com.demo.service.wapBind.dao;


import org.apache.ibatis.annotations.Param;


public interface WapBindMapper {
    
	/**
	 * 根据客户传信息,查询客户表是否有该客户
	 * @param wapOpenid
	 * @param customer_phone
	 * @return
	 */
	String selectByWapBindInfo(@Param("wapOpenid")String wapOpenid, @Param("customer_phone")String customer_phone);
	
	/**
	 * -----------------------------------------修改投保单openid
	 * 更新wap支付投保单号为只是openid
	 * @param wapOpenid
	 * @param realOpenid
	 * @return
	 */
	int updateProposalByOpenid(@Param("wapOpenid")String wapOpenid, @Param("realOpenid")String realOpenid, @Param("suitId")String suitId);
	
    /**
     * -----------------------------------------修改保单openid
     * wap支付，将fake的openid更新为真实openid
     * @param wapOpenid
     * @param realOpenid
     */
    int updatePolicyByOpenid(@Param("wapOpenid")String wapOpenid, @Param("realOpenid")String realOpenid, @Param("suitId")String suitId);
    
    /**
     * 根据订单号查询保单号
     * @param tradeId
     * @return
     */
    String selectPolicyIdByTradeId(String tradeId);
}