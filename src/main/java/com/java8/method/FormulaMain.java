package com.java8.method;

import java.util.Random;

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
		
		
	}
	
}

