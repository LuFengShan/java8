package com.java8.function;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

/**
 * 创建一个对象（工厂类）
 * 设计思想:封装工厂创建对象的逻辑
 * {@link Supplier} 这是一个功能接口，其功能方法是get（）。
 */
public class SupplierTest {
	@Test
	public void test(){
		Supplier<Person> personSupplier = Person::new;
		Person person = personSupplier.get();
		person.setFirstName("gx");
		person.setLastName("s");
		System.out.println(person.toString());

	}
}
