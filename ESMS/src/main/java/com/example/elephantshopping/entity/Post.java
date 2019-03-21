package com.example.elephantshopping.entity;

import java.util.Date;

public class Post {
	// 帖子id
	String pid;
	// 帖子类型 为3类 1:2:3
	int ptype;
	// 帖子主题
	String theme;
	// 帖子内容
	String pcontent;
	// 帖子时间
	Date ptime;
	// 上传图片
	String filename1;

	String filename2;
	// 用户id
	String uid;

	public Post(String pid, int ptype, String theme, String pcontent, Date ptime, String filename1, String filename2,
			String uid) {
		super();
		this.pid = pid;
		this.ptype = ptype;
		this.theme = theme;
		this.pcontent = pcontent;
		this.ptime = ptime;
		this.filename1 = filename1;
		this.filename2 = filename2;
		this.uid = uid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getPtype() {
		return ptype;
	}

	public void setPtype(int ptype) {
		this.ptype = ptype;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getPcontent() {
		return pcontent;
	}

	public void setPcontent(String pcontent) {
		this.pcontent = pcontent;
	}

	public Date getPtime() {
		return ptime;
	}

	public void setPtime(Date ptime) {
		this.ptime = ptime;
	}

	public String getFilename1() {
		return filename1;
	}

	public void setFilename1(String filename1) {
		this.filename1 = filename1;
	}

	public String getFilename2() {
		return filename2;
	}

	public void setFilename2(String filename2) {
		this.filename2 = filename2;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
