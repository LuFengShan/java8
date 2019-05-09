package com.java8.fastjson;

import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * <p>
 * {@link com.alibaba.fastjson.annotation.JSONType} : 可以在对象上面直接放，这样出来的json直接返回的是字符串。
 *
 * <p>
 * {@link SerializerFeature#BeanToArray}:普通模式下，JavaBean映射成json object，BeanToArray模式映射为json array。
 * 也就是普通模式下带上对象的属性值，如{"firstName":"san","lastName":"zhang"}；
 * BeanToArray模式下是只显示kv中的v值，返回的是一个字符串，如["san","zhang"]
 */
//@JSONType(serialzeFeatures = SerializerFeature.BeanToArray,
//        parseFeatures = Feature.SupportArrayToBean)
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

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return lastName + firstName;
    }


}

