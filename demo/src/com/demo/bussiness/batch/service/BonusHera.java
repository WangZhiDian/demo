package com.demo.bussiness.batch.service;

import java.io.Serializable;

public class BonusHera implements Serializable {
	
	private static final long serialVersionUID = 1L;

	String cname;
	
	String addr;

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	

}
