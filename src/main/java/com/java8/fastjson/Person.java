package com.java8.fastjson;

// @JSONType(serialzeFeatures = SerializerFeature.BeanToArray,
// parseFeatures = Feature.SupportArrayToBean)
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

  Person() {}

  Person(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public String toString() {
    return lastName + firstName;
  }


}

