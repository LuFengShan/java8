package com.java8.collection;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @Author sgx
 * @Date 2019/5/24 14:13
 * @Version
 **/
public class IdentityHashMapDemo {
	public static void main(String[] args) {
		String address = "http://localhost:";
		//address + "/alarmFaultsPlatform/alarmFaultsTimeFlag.do"
		Map<String, String> alarmParams = new IdentityHashMap<>();//初始化容器的大小
		alarmParams.put(address + "/alarmFaultsPlatform/alarmFaultsTimeFlag.do", "week");
		alarmParams.put(address + "/alarmFaultsPlatform/alarmFaultsTimeFlag.do", "month");
		alarmParams.put(address + "/alarmFaultsPlatform/alarmFaultsTimeFlag.do", "quarter");

		alarmParams.entrySet().forEach(kv -> System.out.println(kv.getKey() + " : " + kv.getValue()));
		System.out.println(alarmParams.size());

		String str1 = "ABC";
		String str2 = new String("ABC");
		System.out.println(str1 == str2);
		System.out.println(str1.hashCode());
		System.out.println(str2.hashCode());
		str1 = null;
		str2 = null;
		str1 = "ABC";
		System.out.println(str1.hashCode());
	}
}
