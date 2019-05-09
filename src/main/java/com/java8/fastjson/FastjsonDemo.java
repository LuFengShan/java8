package com.java8.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * fastjson示例
 */
public class FastjsonDemo {
    private static final Logger logger = LoggerFactory.getLogger(FastjsonDemo.class);
    private Person p;
    private Person p1;

    @BeforeEach
    public void initPerson() {
        p = new Person();
        p.setFirstName("san");
        p.setLastName("zhang");
        p1 = new Person();
        p1.setFirstName("si");
        p1.setLastName("li");
    }

    /**
     * 直接把对象序列化成json格式
     */
    @Test
    public void testObjectMapJson() {
        logger.info(() -> JSON.toJSONString(p));
        logger.info(() -> JSON.toJSONString(p1));
    }

    /**
     * 把对象转化为json格式 ，但是没有key值，只存value
     */
    @Test
    public void testObjectMapJsonWithSerializerFeature() {
        String string = JSON.toJSONString(p, SerializerFeature.BeanToArray);
        logger.info(() -> string); // ["san", "zhang"]
        if (string.startsWith("["))
            logger.info(() -> "true");
    }

    /**
     * 一对多的一个转换
     */
    @Test
    public void testArraysMapJson() {
        PersonList personList = new PersonList();

        List<Person> list = new ArrayList<>();
        list.add(p);
        list.add(p1);

        personList.setCone(1001);
        personList.setList(list);

        logger.info(() -> JSON.toJSONString(personList)); // {"cone":1001,"list":[["san","zhang"],["si","li"]]}
    }

}
