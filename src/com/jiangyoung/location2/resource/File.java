package com.jiangyoung.location2.resource;

public class File {
	
	private String name;
	private String fullName;
	private String CDNUrl;
	private int length;
	private int uploadTime;
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
	public String getCDNUrl() {
		return CDNUrl;
	}
	public void setCDNUrl(String cDNUrl) {
		CDNUrl = cDNUrl;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(int uploadTime) {
		this.uploadTime = uploadTime;
	}
	@Override
	public String toString() {
		return "File [name=" + name + ", fullName=" + fullName + ", CDNUrl="
				+ CDNUrl + ", length=" + length + ", uploadTime=" + uploadTime
				+ "]";
	}
	
	
	

}
