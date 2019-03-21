package com.example.elephantshopping.entity;

/**
 * 操作日志
 * 
 * @author XB
 *
 */
public class Log {
	// 操作记录表主键
	private String EORID;
	// 操作人
	private String USERS_ID;
	// 操作时间
	private String CTIME;
	// 动作名称
	private String ACTION_NAME;
	// 备注
	private String NOTE;
	// IP
	private String IP;

	public String getEORID() {
		return EORID;
	}

	public void setEORID(String eORID) {
		EORID = eORID;
	}

	public String getUSERS_ID() {
		return USERS_ID;
	}

	public void setUSERS_ID(String uSERS_ID) {
		USERS_ID = uSERS_ID;
	}

	public String getCTIME() {
		return CTIME;
	}

	public void setCTIME(String cTIME) {
		CTIME = cTIME;
	}

	public String getACTION_NAME() {
		return ACTION_NAME;
	}

	public void setACTION_NAME(String aCTION_NAME) {
		ACTION_NAME = aCTION_NAME;
	}

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String nOTE) {
		NOTE = nOTE;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}
}
