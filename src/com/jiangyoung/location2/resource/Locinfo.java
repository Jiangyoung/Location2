package com.jiangyoung.location2.resource;

public class Locinfo {
	private int id;
	private String time;
	private int errorcode;
	private double latitude;
	private double lontitude;
	private float radius;
	private float speed;
	private int satellite;
	private String addr;
	
	
	public Locinfo() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public int getErrorcode() {
		return errorcode;
	}


	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLontitude() {
		return lontitude;
	}


	public void setLontitude(double lontitude) {
		this.lontitude = lontitude;
	}


	public float getRadius() {
		return radius;
	}


	public void setRadius(float radius) {
		this.radius = radius;
	}


	public float getSpeed() {
		return speed;
	}


	public void setSpeed(float speed) {
		this.speed = speed;
	}


	public int getSatellite() {
		return satellite;
	}


	public void setSatellite(int satellite) {
		this.satellite = satellite;
	}


	public String getAddr() {
		return addr;
	}


	public void setAddr(String addr) {
		this.addr = addr;
	}

	
	
}
