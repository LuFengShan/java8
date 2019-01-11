package com.java8.function;

import java.util.StringJoiner;

/**
 * 开发者 : SGX <BR>
 * 时间：2018年6月26日 上午8:47:48 <BR>
 * 变更原因： <BR>
 * 首次开发时间：2018年6月26日 上午8:47:48 <BR>
 * 描述： 构造方法在java8中的调用<BR>
 * 版本：V1.0
 */
public class Person {
	String firstName;
	String lastName;


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	Person() {
	}

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
				.add("firstName='" + firstName + "'")
				.add("lastName='" + lastName + "'")
				.toString();
	}
}

