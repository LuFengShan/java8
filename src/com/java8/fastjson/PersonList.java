package com.java8.fastjson;

import java.util.ArrayList;
import java.util.List;

public class PersonList {
  private int cone;
  private List<Person> list = new ArrayList<>();

  public int getCone() {
    return cone;
  }

  public void setCone(int cone) {
    this.cone = cone;
  }

  public List<Person> getList() {
    return list;
  }

  public void setList(List<Person> list) {
    this.list = list;
  }

}

