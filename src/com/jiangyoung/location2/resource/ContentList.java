package com.jiangyoung.location2.resource;

import java.util.List;

public class ContentList {
	
	private int dirNum;
	private int fileNum;
	private List<Dir> dirs;
	private List<File> files;
	
	public int getDirNum() {
		return dirNum;
	}
	public void setDirNum(int dirNum) {
		this.dirNum = dirNum;
	}
	public int getFileNum() {
		return fileNum;
	}
	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}
	public List<Dir> getDirs() {
		return dirs;
	}
	public void setDirs(List<Dir> dirs) {
		this.dirs = dirs;
	}
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
	@Override
	public String toString() {
		return "ContentList [dirNum=" + dirNum + ", fileNum=" + fileNum
				+ ", dirs=" + dirs + ", files=" + files + "]";
	}
	
	

}
