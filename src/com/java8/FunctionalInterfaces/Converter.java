package com.java8.FunctionalInterfaces;

/**
 * 开发者 : SGX <BR>
 * 时间：2018年6月26日 上午8:33:32 <BR>
 * 变更原因： <BR>
 * 首次开发时间：2018年6月26日 上午8:33:32 <BR>
 * 描述： 转换器 ,功能性接口<BR>
 * 版本：V1.0
 */
public interface Converter<F, T> {
  T converter(F from);
}

