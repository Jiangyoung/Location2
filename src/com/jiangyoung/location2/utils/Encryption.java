package com.jiangyoung.location2.utils;

import android.util.Base64;

public class Encryption {
	private String key ;
	private String text;
	private int keyLength ;
	
	public Encryption(String key, String text) {
		super();
		this.key = key;
		this.text = text;
		this.keyLength = key.length();

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String replaceEncrypt() throws Exception{
		int i = 0, j = 0;
		//先把数据转为base64编码
		String base64text = new String(Base64.encode(this.text.getBytes(), Base64.DEFAULT),"UTF-8");
		//****************************************************************************
		//转换编码后多了一位 换行
		base64text = base64text.substring(0,base64text.length()-1);
		
		String md5Base64text = MD5.md5(base64text);
		
		//处理后的数据  32位原数据的md5 + 转为base64编码的数据 + 32位转为base64编码数据的md5
		String str = MD5.md5(this.text) + base64text + md5Base64text;
		
		StringBuffer sb = new StringBuffer();
		
		int strLength = str.length();
		//TODO 关于java与php ascii码值上的偏移乱码问题
		////////关于 %4    最大为 %6 即为最大偏移5位   超过一定值java与php处理方式不同
		// %127 处理让偏移之后还在ascii码值的范围内 ----不可用
		for(i=0;i<strLength;i++){
			sb.append((char) ( (str.charAt(i) + this.key.charAt(j++)) %4));
			if(keyLength == j)j=0;
		}
		
		String ciphertext = sb.toString();
		
		return ciphertext;
	}
	public String replaceDecrypt() throws Exception{
		int i = 0, j = 0;
		
		StringBuffer sb = new StringBuffer();
		
		int textLength = this.text.length();

		for(i=0;i<textLength;i++){
			sb.append((char) (this.text.charAt(i) - this.key.charAt(j++)%4));
			if(keyLength == j)j=0;
		}
		
		//处理后的数据  32位原数据的md5 + 转为base64编码的数据 + 32位转为base64编码数据的md5
		String str = sb.toString();
		
		String base64text = str.substring(32,str.length()-32);
		
		String md5Text = str.substring(0,32);
		
		String md5Base64text = str.substring(str.length()-32);
		
		if(!md5Base64text.equals(MD5.md5(base64text))){
			throw new  Exception("数据不完整1"+md5Base64text);
		}
		
		String cleartext = new String( Base64.decode(base64text, Base64.DEFAULT),"UTF-8");
		
		if(!md5Text.equals(MD5.md5(cleartext))){
			throw new  Exception("数据不完整2");
		}
		
		return cleartext;
	}
}
