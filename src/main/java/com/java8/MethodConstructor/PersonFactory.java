package com.java8.MethodConstructor;

/*
 * 人员构造工厂
 */
public interface PersonFactory<P extends Person> {
  // 这样写的话，会默认调用有参考构造器
  P create(String ming, String xing);
}

