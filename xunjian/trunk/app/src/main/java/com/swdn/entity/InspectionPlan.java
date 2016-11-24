package com.swdn.entity;

import java.io.Serializable;
import java.util.Date;

public class InspectionPlan implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int state;
	private String type;
	private String name;
	private String date;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
