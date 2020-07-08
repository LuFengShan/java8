package com.java8.suijishu;

/**
 * 状态
 */
public enum ResultStatus {
	/**
	 * 请求成功
	 */
	SUCCESS("OK"),
	/**
	 * 请求失败
	 */
	FAIL("FAILED"),
	/**
	 * 未登录
	 */
	NOLOG("UNAUTHORIZED");

	private String status;

	ResultStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
