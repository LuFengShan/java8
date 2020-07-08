package com.java8.suijishu;

import lombok.Data;

/**
 * 封装统一的接口，返回状态，接口说明及数据
 * <p>状态参考：{@link ResultStatus}</p>
 *
 * @param <T>
 */
@Data
public class ResultBean<T> {

	/**
	 * 接口返回状态
	 */
	private String code = ResultStatus.SUCCESS.getStatus();
	/**
	 * 接口说明
	 */
	private String msg = "";
	/**
	 * 接口封装的数据
	 */
	private T data;

	public ResultBean() {
		super();
	}

	public ResultBean(T data) {
		super();
		this.data = data;
	}

	public ResultBean(T data, String msg) {
		super();
		this.data = data;
		this.msg = msg;
	}

	public ResultBean(Throwable e, ResultStatus responseStatus) {
		super();
		this.msg = e.toString();
		this.code = responseStatus.getStatus();
	}
}
