package com.java8.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	@Test
	public void testGPSToGd() {
		String dir = "C:/Users/Administrator/Documents/异地行驶(2)";
		String tarDir = "d:/异地行驶(2)";
		List<String> list = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : stream) {
				if (!Files.isDirectory(path)) { // 判断这个文件是不是一个文件夹，如果不是文件夹就存入文件的名字
					list.add(path.getFileName().toString());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String pathFile : list) {

			// 读取文件的位置
			// String pathFile = "C:\\Users\\Administrator\\Documents\\异地行驶(2)\\js180.json";
			Path pathRead = Paths.get(dir + File.separator + pathFile);
			logger.info(() -> "读取json文件的位置 ： " + pathRead.toString());
			// 生成文件的位置
			Path pathWrite = Paths.get(tarDir + File.separator+ pathFile);
			logger.info(() -> "生成文件的位置 ： " + pathWrite.toString());
			try (BufferedReader reader = Files.newBufferedReader(pathRead);
				 BufferedWriter writer = Files.newBufferedWriter(pathWrite)) {
				// 读取json文档中的所有内容
				String collect = reader.lines()
						.collect(Collectors.joining());
				// json内容转换成对象
				List<GpsDome> gpsDomeList = JSON.toJavaObject(JSONObject.parseObject(collect), JsonRootBean.class)
						// 取得所有的坐标
						.getDots()
						.stream()
						// 转换成我们要的高德的坐标
						.map(dots -> PositionUtil.gps84_To_Gcj021(dots.getType()
								, div(String.valueOf(dots.getY()), String.valueOf(1000000.00), 6).doubleValue()
								, div(String.valueOf(dots.getX()), String.valueOf(1000000.00), 6).doubleValue()))
						.filter(Objects::nonNull)
						.collect(Collectors.toList());
				// 处理后以转换成json
				String result = JSON.toJSONString(gpsDomeList);
				// 把json存入文档中
				writer.write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		logger.info(() -> "文件转换完毕！");
	}

	public static BigDecimal div(String value1, String value2, int scale) {
		BigDecimal b1 = new BigDecimal(value1);
		BigDecimal b2 = new BigDecimal(value2);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_EVEN);
	}
}
