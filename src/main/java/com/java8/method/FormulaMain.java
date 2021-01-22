package com.java8.method;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FormulaMain {

	public static void main(String[] args) {
		Formula formula = new Formula() {

			@Override
			public double calculate(int a) {
				return sqrt(a * 100);
			}
		};
		System.out.println(formula.calculate(16));


		int i = new Random().ints(1, 60).findAny().getAsInt();
		System.out.println(i);

		/**
		 * 生成100-200w内的随机数
		 */
		System.out.println(ThreadLocalRandom.current().nextInt(1000000, 2000000));

		Long reply = 99990000L;
		Long reply2 = 100000000L;
		if (reply2 - reply > 0L) {
			System.out.println(ThreadLocalRandom.current().nextLong(1000000, 2000000));
		}
		System.out.println(Long.MAX_VALUE);


	}

}

