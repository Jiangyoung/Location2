package com.jiangyoung.location2.resource;

public class Dir {
	private String name;
	private String fullName;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	@Override
	public String toString() {
		return "Dir [name=" + name + ", fullName=" + fullName + "]";
	}
	
	

}
