package com.java8.builin.FunctionInterface;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * 生产者，消费者
 */
public class SuppliersConsumerTest {

  public static void main(String[] args) {
    Supplier<Person> supplier = Person::new;
    Person person = supplier.get();
    person.setFirstName("哈");
    person.setLastName("啊");


    Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
    greeter.accept(person);

    Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

    Person p1 = new Person("John", "Doe");
    Person p2 = new Person("Alice", "Wonderland");

    comparator.compare(p1, p2); // > 0
    comparator.reversed().compare(p1, p2); // < 0
  }

}

