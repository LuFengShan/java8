/**
 * ProjectName:java8learn<BR>
 * File name: PersonTest.java <BR>
 * Author: SGX <BR>
 * Project:java8learn <BR>
 * Version: v 1.0 <BR>
 * Date: 2018年6月26日 上午8:49:45 <BR>
 * Description: <BR>
 * Function List: <BR>
 */

package com.java8.MethodConstructor;

public class PersonTest {

  public static void main(String[] args) {
    PersonFactory<Person> person = Person::new;
    Person create = person.create("哈", "啊");
    System.out.println(create.toString());
  }

}

