package com.jiangyoung.location2.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;



import android.util.Xml;

import com.jiangyoung.location2.resource.ContentList;
import com.jiangyoung.location2.resource.Dir;
import com.jiangyoung.location2.resource.File;

public class PullListXML {
	/**
	 * 
	 * 解析InputStream格式的xml数据
	 * 返回对象ContentList list
	 * /
	public static ContentList getContentList(InputStream xml) throws Exception {
		ContentList list = null;
		List<Dir> dirs = null;
		List<File> files = null;
		Dir dir = null;
		File file = null;
		
		XmlPullParser pullParser = Xml.newPullParser();
		pullParser.setInput(xml,"utf-8");
		
		int event = pullParser.getEventType();
		
		while(event != XmlPullParser.END_DOCUMENT){
			switch(event){
			case XmlPullParser.START_DOCUMENT:
				list = new ContentList();
				break;
			case XmlPullParser.START_TAG:
				if("dirNum".equals(pullParser.getName())){
					list.setDirNum(Integer.parseInt(pullParser.nextText()));
				}
				if("fileNum".equals(pullParser.getName())){
					list.setFileNum(Integer.parseInt(pullParser.nextText()));
				}
				if("dirs".equals(pullParser.getName())){
					dirs = new ArrayList<Dir>();
				}
				if("files".equals(pullParser.getName())){
					files = new ArrayList<File>();
				}
				if("dir".equals(pullParser.getName())){
					dir = new Dir();
				}
				if("file".equals(pullParser.getName())){
					file = new File();
				}
				if("fileName".equals(pullParser.getName())){
					file.setName(pullParser.nextText());
				}
				if("fileFullName".equals(pullParser.getName())){
					file.setFullName(pullParser.nextText());
				}
				if("fileCDNUrl".equals(pullParser.getName())){
					file.setCDNUrl(pullParser.nextText());
				}
				if("fileLength".equals(pullParser.getName())){
					file.setLength(Integer.parseInt(pullParser.nextText()));
				}
				if("fileUploadTime".equals(pullParser.getName())){
					file.setUploadTime(Integer.parseInt(pullParser.nextText()));
				}
				if("dirName".equals(pullParser.getName())){
					dir.setName(pullParser.nextText());
				}
				if("dirFullName".equals(pullParser.getName())){
					dir.setFullName(pullParser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if("file".equals(pullParser.getName())){
					files.add(file);
				}
				if("dir".equals(pullParser.getName())){
					dirs.add(dir);
				}
				if("files".equals(pullParser.getName())){
					list.setFiles(files);
				}
				if("dirs".equals(pullParser.getName())){
					list.setDirs(dirs);
				}
				break;
			}
			event = pullParser.next();
		}
		
		return list;		
	}
}
