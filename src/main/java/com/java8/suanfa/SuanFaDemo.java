package com.java8.suanfa;


public class SuanFaDemo {
	// �ص��㷨
	public static int jicheng(int i) {
		if (i == 1) {
			return 1;
		} else {
			return i * jicheng(i - 1);
		}
	}

}
