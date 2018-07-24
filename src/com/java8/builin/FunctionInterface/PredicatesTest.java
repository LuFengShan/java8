/**
 * ProjectName:java8learn<BR>
 * File name: PredicatesTest.java <BR>
 * Author: SGX <BR>
 * Project:java8learn <BR>
 * Version: v 1.0 <BR>
 * Date: 2018年6月26日 上午8:58:27 <BR>
 * Description: <BR>
 * Function List: <BR>
 */

package com.java8.builin.FunctionInterface;

import java.util.Objects;
import java.util.function.Predicate;

public class PredicatesTest {
  public static void main(String[] args) {
    Predicate<String> predicate = (s) -> s.length() > 0;
    System.out.println(predicate.test("lala"));
    Predicate<String> predicate1 = (s) -> s.startsWith("l");
    // and
    // 在评估组合谓词时，如果此谓词为假，则不评估另一个谓词。
    // 评估过程中评估中继给调用者的任何异常;如果对此谓词的评估抛出异常，则不会评估其他谓词。
    System.out.println(predicate1.and(predicate).test("ss"));
    // negate是逻辑否决谓词，取相反值
    System.out.println(predicate.negate().test("ss"));

    Predicate<Boolean> nonNull = Objects::nonNull;
    System.out.println("nonNull : " + nonNull.test(null));
    Predicate<Boolean> isNull = Objects::isNull;
    System.out.println("isNull : " + isNull.test(null));


    Predicate<String> isEmpty = String::isEmpty;
    System.out.println("isEmpty : " + isEmpty.test("lala"));
    Predicate<String> isEmptynegate = isEmpty.negate();
    System.out.println("isEmptyNegate : " + isEmptynegate.test("lala"));

  }

}

