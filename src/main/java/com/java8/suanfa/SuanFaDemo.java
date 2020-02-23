package com.java8.suanfa;


public class SuanFaDemo {
	// »Øµ÷Ëã·¨
	public static int jicheng(int i) {
		if (i == 1) {
			return 1;
		} else {
			return i * jicheng(i - 1);
		}
	}

}
