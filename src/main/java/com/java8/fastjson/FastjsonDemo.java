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
import java.util.regex.Pattern;
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

	@Test
	public void testObjectMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("phone", "582989326");
		Map<String, String> map1 = new HashMap<>();
		map1.put("chenghu", "sgx");
		map1.put("bandName", "wow");
		String s = JSON.toJSONString(map1);
		s = s.replace('\"','\'');
		map.put("spliceMap", s);
		System.out.println(JSON.toJSONString(map));
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
	public void testGPSToBd() {
		String dir = "C:\\Users\\Administrator\\Desktop\\经纬度\\h2data";
		String tarDir = "d:/经纬度";
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
			Path pathRead = Paths.get(dir + File.separator + pathFile);
			logger.info(() -> "读取文件的位置 ： " + pathRead.toString());
			// 生成文件的位置
			Path pathWrite = Paths.get(tarDir + File.separator + pathFile);
			logger.info(() -> "生成文件的位置 ： " + pathWrite.toString());
			try (BufferedReader reader = Files.newBufferedReader(pathRead);
			     BufferedWriter writer = Files.newBufferedWriter(pathWrite)) {
				// 读取文档中的所有内容
				List<List<Double>> lonLats = reader.lines()
						.skip(1)
						.map(s -> {
							List<String> strings = Pattern.compile(",")
									.splitAsStream(s)
									.collect(Collectors.toList());
							Gps gps = PositionUtil.gcj02_To_Bd09(Double.parseDouble(strings.get(3)), Double.parseDouble(strings.get(2)));
							return Arrays.asList(gps.getWgLon(), gps.getWgLat());
							// return String.join(",", String.valueOf(gps.getWgLon()), String.valueOf(gps.getWgLat()));
						})
						.collect(Collectors.toList());
				// 处理后以转换成json
				String result = JSON.toJSONString(lonLats);
				// 把json存入文档中
				writer.write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		logger.info(() -> "文件转换完毕！");
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
			Path pathWrite = Paths.get(tarDir + File.separator + pathFile);
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


	@Test
	public void demo() {
		String abVoltage = "-0.8407_-0.7713_-0.7785_-1.8508_-0.9836_0.1431_-1.116_1.2101_0.4105_-0.6777_0.5846_-1.5804_0.59_-0.2254_1.276_0.2406_-1.0498_0.1518_-0.4241_-0.2254_-1.0498_-0.0998_0.0633_-0.4071_0.5846_-1.1017_-1.1409_-1.6047_-0.0727_-0.699_-0.5476_-0.2254_-0.5464_-0.0905_0.0783_-0.2108_0.4295_0.1678_0.7175_-0.8913_-0.1567_-2.1825_2.1573_0.9555_0.6824_-0.5911_0.9569_-0.4775_1.8271_1.8032_0.1685_0.6004_-0.773_-0.2332_-0.3162_0.4732_0.6824_0.6004_0.7449_1.6444_0.5342_0.7587_0.2848_0.4172_0.2572_0.9757_0.5846_0.052_-0.0844_0.3369_-0.67_0.6056_0.9432_1.7249_0.069_0.0502_0.6776_0.069_-0.2026_-0.1567_0.8307_0.1935_2.0751_0.7666_-5.1494_-1.6331_-0.3849_-0.6155_-1.2301_-0.0673_1.0256_-0.069_0.6135_0.0278_-0.0243_0.411";
		String[] abString = abVoltage.split("_");
		HashMap<Integer, Double> map;
		for (int i = 0; i < abString.length; i++) {
			map = new HashMap<>();
			map.put(i + 1, Double.valueOf(abString[i]));
			System.out.println(JSON.toJSONString(map));
		}
	}

	@Test
	public void sanxian() {

	}
}
