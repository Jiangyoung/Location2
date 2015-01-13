package com.jiangyoung.location2.resource;

public class StatusID {
	/**
	 * 加密私钥
	 */
	public final static String MY_KEY = "jiangyoungzhlocationapplication";
	/**
	 * 获取位置信息失败
	 */
	public final static int GET_LOCINFO_FAILED = 0;
	/**
	 * 成功取得位置信息
	 */
	public final static int GET_LOCINFO_SUCC = 1;
	/**
	 * 权限信息请求成功
	 */
	public final static int REQUEST_POWINFO_SUCC = 2;
	/**
	 * 权限信息请求失败
	 */
	public final static int REQUEST_POWINFO_FAILED = 3;
	/**
	 * 权限信息验证失败
	 */
	public final static int VERIFY_POWINFO_SUCC = 4;
	/**
	 * 权限信息验证成功	
	 */
	public final static int VERIFY_POWINFO_FAILED =5;
	/**
	 * 有操作权限
	 */
	public final static int POWER_ISVALID = 6;
	/**
	 * 无操作权限
	 */
	public final static int POWER_INVALID = 7;
	/**
	 * 获取列表成功
	 */
	public final static int GET_LIST_SUCC =8;
	/**
	 * 获取列表失败
	 */
	public final static int GET_LIST_FAILED =9;
	/**
	 * 成功使token失效
	 */
	public final static int ABANDON_TOKEN_SUCC = 10;
	/**
	 * 使token失效失败
	 */
	public final static int ABANDON_TOKEN_FAILED = 11;
	/**
	 * 需要重新认证
	 */
	public final static int NEED_REVALID = 12;
	/**
	 * 认证有效时间  ms
	 */
	public final static long PERIOD_VALID = 20000;

}
