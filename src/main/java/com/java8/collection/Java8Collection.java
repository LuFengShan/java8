package com.java8.collection;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Java8Collection {
	private static List<Person> people;

	@BeforeEach
	public void before() {
		people =
				Arrays.asList(
						new Person("石峰", "张", 30),
						new Person("lili", "luck", 20),
						new Person("老大", "张", 44),
						new Person("石头", "孙", 55),
						new Person("老二", "张", 28),
						new Person("汪", "王", 33),
						new Person("汪", "王", 38),
						new Person("光明", "孙", 22),
						new Person("丘吉尔", "孙", 20),
						new Person("权", "孙", 20),
						new Person("力", "王", 35)
				);
	}

	/**
	 * GroupingBy收集器用于按某些属性对对象进行分组，并将结果存储在Map实例中。
	 */
	@Test
	public void testGroupingBy() {
		// 1. 筛选出为不为null的对象
		List<Person> collect = people.stream().filter(Objects::nonNull).collect(Collectors.toList());
		//collect.forEach(System.out::println);

		// 2. 根据lastName分组，过虑掉lastName为null的
		Map<String, List<Person>> collect1 = people.stream()
				.filter(person -> !Objects.equals(null, person.getLastName())) // 过虑lastName为null的
				.collect(Collectors.groupingBy(Person::getLastName));// 分组函数
		//collect1.entrySet().forEach(System.out::println);
		Map<String, List<Person>> collect11 = people.stream()
				.filter(person -> !Objects.equals(null, person.getLastName())) // 过虑lastName为null的
				.collect(Collectors.groupingByConcurrent(Person::getLastName));// 分组函数
		//collect11.entrySet().forEach(System.out::println);

		// 3.根据lastName分组，把组中的firstName用"、"拼接起来
		Map<String, String> collect2 = people.stream()
				.filter(person -> !Objects.equals(null, person.getLastName()))
				.collect(Collectors.groupingBy(Person::getLastName
						, Collectors.mapping(Person::getFirstName, Collectors.joining("、"))
						)
				);
		// collect2.entrySet().forEach(System.out::println);

		// 4.根据lastName分组，把组中的firstName用"、"拼接起来，并在拼接完成的字符串上面加上前后缀
		Map<String, String> collect3 = people.stream()
				.filter(person -> !Objects.equals(null, person.getLastName()))
				.collect(Collectors.groupingBy(Person::getLastName
						, Collectors.mapping(Person::getFirstName
								, Collectors.joining("、", "firstName: [", "]")
						)
						)
				);
		// collect3.entrySet().forEach(System.out::println);

		// 5.根据lastName分组，取出各组中的年龄和
		Map<String, Integer> collect4 = people.stream()
				.filter(person -> !Objects.equals(null, person.getLastName()))
				.collect(Collectors.groupingBy(Person::getLastName, Collectors.summingInt(Person::getAge)));
		collect4.entrySet()
				.forEach(map -> System.out.println("lastName : " + map.getKey() + ", 年龄和 ： " + map.getValue()));

		// 6.
		Map<Integer, Set<Person>> ollect5 = people.stream()
				.collect(Collectors.groupingBy(Person::getAge, Collectors.toSet()));
		// collect5.entrySet().forEach(System.out::println);

		// 7. 根据lastName分组，取出各组中年龄最大的那个人
		Map<String, Optional<Person>> collect6 = people.stream()
				.filter(person -> !Objects.equals(null, person.getLastName()))
				.collect(Collectors.groupingBy(Person::getLastName
						, Collectors.maxBy(Comparator.comparingInt(Person::getAge))
						)
				);
		// collect6.entrySet().forEach(System.out::println);

		// 8.根据lastName分组，统计各组有多少人
		Map<String, Long> collect7 = people.stream()
				.collect(Collectors.groupingBy(Person::getLastName, Collectors.counting()));
		collect7.entrySet()
				.forEach(s ->
						System.out.println("lastName为 " + s.getKey() + " 的人有 " + s.getValue() + " 个"));

		// 8.先根据lastName分组，在把各组中的人员根据年龄分组
		Map<String, Map<Integer, List<Person>>> collect8 = people.stream()
				.collect(Collectors.groupingBy(Person::getLastName
						, Collectors.groupingBy(Person::getAge)
						)
				);
		collect8.entrySet().forEach(System.out::println);


		long sum = people.stream()
				.mapToLong(Person::getAge)
				.sum();
		System.out.println(sum);


	}

	@Test
	public void test6() {
		List<Person> hahah = people.stream()
				.map(person -> {
					person.setFirstName("hahah");
					return person;
				})
				.collect(Collectors.toList());

		hahah.forEach(System.out::println);

	}

	@Test
	public void testGroupByLimit() {
		List<RunNameMonth> list = new ArrayList<>(2000000);
		for (int i = 1; i < 2000001; i++) {
			if (i < 200000) {
				list.add(new RunNameMonth("2018-01", "比亚迪汽车有限公司1", i * 2.00));
			} else if (i < 400000) {
				list.add(new RunNameMonth("2018-02", "比亚迪汽车有限公司2", i * 3.00));
			} else if (i < 600000) {
				list.add(new RunNameMonth("2018-03", "比亚迪汽车有限公司3", i * 4.00));
			} else if (i < 800000) {
				list.add(new RunNameMonth("2018-04", "比亚迪汽车有限公司4", i * 5.00));
			} else if (i < 1000000) {
				list.add(new RunNameMonth("2018-05", "比亚迪汽车有限公司5", i * 6.00));
			} else if (i < 1200000) {
				list.add(new RunNameMonth("2018-06", "比亚迪汽车有限公司6", i * 7.00));
			} else if (i < 1500000) {
				list.add(new RunNameMonth("2018-07", "比亚迪汽车有限公司7", i * 8.00));
			} else if (i < 200000) {
				list.add(new RunNameMonth("2018-08", "比亚迪汽车有限公司8", i * 9.00));
			} else {
				list.add(new RunNameMonth("2018-09", "比亚迪汽车有限公司9", i * 9.50));
			}
		}

		list.parallelStream()
				.sorted(Comparator.comparingDouble(RunNameMonth::getRunKm).reversed())
				.collect(Collectors.groupingBy(RunNameMonth::getYearMonth,// 首先根据月份分组
						Collectors.groupingBy(RunNameMonth::getUnName, // 再次根据单位分组
								Collectors.summingDouble(RunNameMonth::getRunKm) // 根据月份和单位分组，然后取出里程和
						))
				)
				.entrySet()
				.stream()
				.map(entry ->
						{
							List<RunNameMonth> list1 = new ArrayList<>();
							entry.getValue()
									.entrySet()
									.stream()
									.forEach(e2 -> list1.add(new RunNameMonth(entry.getKey(), e2.getKey(), e2.getValue())));
							return list1;
						}
				)
				.forEach(System.out::println);

//		people.parallelStream()
//				.sorted(Comparator.comparingInt(Person::getAge).reversed())
//				.collect(Collectors.groupingBy(Person::getLastName))
//				.entrySet()
//				.stream()
//				.flatMap(kv -> kv.getValue()
//						.stream()
//						.limit(2)
//				)
//				.collect(Collectors.toList())
//				.forEach(System.out::println);
	}

	/**
	 * PartitioningBy是groupingBy的一个特殊情况，它接受Predicate实例并将Stream元素收集到Map实例中，该实例将布尔值存储为键，集合作为值存储。
	 * 在“true”键下，您可以找到与给定谓词匹配的元素集合，
	 * 在“false”键下，您可以找到与给定谓词不匹配的元素集合。
	 */
	@Test
	public void testPartitioningBy() {
		// 把lastName为null的分为一个组，其它的分为一个组
		Map<Boolean, List<Person>> collect = people.stream()
				.collect(Collectors.partitioningBy(person -> Objects.equals(null, person.getLastName())));
		collect.entrySet().forEach(System.out::println);
	}

	/**
	 * 根据lastname分组，求各组中的年龄和
	 */
	@Test
	public void testSumAgeGroupByLastName() {
		Map<String, Integer> collect = people.stream()
				.collect(Collectors.groupingBy(Person::getLastName, Collectors.summingInt(Person::getAge)));
		collect.entrySet().forEach(System.out::println);
	}

	@Test
	public void test1000000Stream() {
		List<Person> list = new ArrayList<>(10010000);
		for (int i = 1; i < 10000001; i++) {
			list.add(new Person("名字" + i, "张" + i, 99));
		}
		System.out.println(LocalTime.now());
		Long collect = list.stream()
				.filter(person -> person.getLastName().startsWith("张2"))
				.collect(Collectors.counting());
		System.out.println(LocalTime.now() + " : " + collect);
	}

	@Test
	public void test1000000parallelStream() {
		List<Person> list = new ArrayList<>(10000000);
		for (int i = 1; i < 10000001; i++) {
			list.add(new Person("名字" + i, "张" + i, 99));
		}
		System.out.println(LocalTime.now());
		Long collect = list.parallelStream()
				.filter(person -> person.getLastName().startsWith("张2"))
				.collect(Collectors.counting());
		System.out.println(LocalTime.now() + " : " + collect);
	}

	@Test
	public void test01() {
		List<OfflineVehicle> list = new ArrayList<>();
		Supplier<OfflineVehicle> supplier = OfflineVehicle::new;
		OfflineVehicle ov = supplier.get();
		ov.setVin("LHB14T3E2GR134804");
		ov.setLicensePlate("测AD589C");
		ov.setTypeName("测试类型一");
		ov.setVehModelName("BJ5022XXYV3R2-BEV");
		ov.setUnName("北京汽车股份有限公司");
		list.add(ov);

		ov = supplier.get();
		ov.setVin("LHB15T3E0JG400661");
		ov.setLicensePlate("测BD589C");
		ov.setTypeName("测试类型二");
		ov.setVehModelName("BJ5030XXYVRRC-BEV");
		ov.setUnName("北汽新能源汽车常州有限公司");
		list.add(ov);
		ov = supplier.get();
		ov.setVin("LKCBANA20HC044583");
		ov.setLicensePlate("测BD579C");
		ov.setTypeName("测试类型二");
		ov.setVehModelName("CH5015XXYBEVA2CD");
		ov.setUnName("江西昌河汽车有限责任公司");
		list.add(ov);
		ov = supplier.get();
		ov.setVin("LHB14T3E2GR141431");
		ov.setLicensePlate("测BD679C");
		ov.setTypeName("测试类型三");
		ov.setVehModelName("BJ5022XXYV3R2-BEV");
		ov.setUnName("北京汽车股份有限公司");
		list.add(ov);
		ov = supplier.get();
		ov.setVin("L6T78Y4W9HN052280");
		ov.setLicensePlate("测BBD679");
		ov.setTypeName("测试类型四");
		ov.setVehModelName("MR7002BEV03");
		ov.setUnName("浙江吉利汽车有限公司");
		list.add(ov);

		List<OfflineVehicle> offlineVehicles = list;

		// 1. 返回所有的车牌号
		List<String> licensePlateCollect = offlineVehicles.parallelStream()
				.map(OfflineVehicle::getLicensePlate) // 映射出所有的车牌号
				.distinct()
				.sorted()
				.collect(Collectors.toList());

		// 2. 返回所有的车辆类别
		List<String> typeNameCollect = offlineVehicles.parallelStream()
				.map(OfflineVehicle::getTypeName) // 映射出所有的车牌号
				.distinct()
				.sorted()
				.collect(Collectors.toList());

		// 3. 返回所有的车辆型号
		List<String> vehModelNameCollect = offlineVehicles.parallelStream()
				.map(OfflineVehicle::getVehModelName) // 映射出所有的车牌号
				.distinct()
				.sorted()
				.collect(Collectors.toList());

		// 4. 返回所有的车辆生产企业
		List<String> unNameCollect = offlineVehicles.parallelStream()
				.map(OfflineVehicle::getUnName) // 映射出所有的车牌号
				.distinct()
				.sorted()
				.collect(Collectors.toList());

		// 5. 返回所有的车辆运营单位
		List<String> unitNameCollect = offlineVehicles.parallelStream()
				.map(OfflineVehicle::getUnitName) // 映射出所有的车牌号
				.distinct()
				.sorted()
				.collect(Collectors.toList());

		Map<String, List<String>> map = new HashMap<>();
		map.put("licensePlateCollect", licensePlateCollect);
		map.put("typeNameCollect", typeNameCollect);
		map.put("vehModelNameCollect", vehModelNameCollect);
		map.put("unNameCollect", unNameCollect);
		map.put("unitNameCollect", unitNameCollect);

		String string = JSON.toJSONString(map);
		System.out.println(string);

		String licensePlate = "BD";
		String typeName = "型二";
		String vehModelName = "";
		String unName = "";
		String unitName = "";

		List<OfflineVehicle> collect = offlineVehicles.parallelStream()
				// 过滤 车牌号
				.filter(offlineVehicle -> {
					if (!Objects.equals("", licensePlate) && !offlineVehicle.getLicensePlate().contains(licensePlate)) {
						return false;
					}
					if (!Objects.equals("", typeName) && !offlineVehicle.getTypeName().contains(typeName)) {
						return false;
					}
					if (!Objects.equals("", vehModelName) && !offlineVehicle.getVehModelName().contains(vehModelName)) {
						return false;
					}
					if (!Objects.equals("", unName) && !offlineVehicle.getUnName().contains(unName)) {
						return false;
					}
					if (!Objects.equals("", unitName) && !offlineVehicle.getUnitName().contains(unitName)) {
						return false;
					}
					return true;
				})
				.collect(Collectors.toList());
		System.out.println(JSON.toJSONString(collect));

	}

	@Test
	public void test5() {
		List<Column> list = new ArrayList<>();
		Column column1 = new Column("乘用车", "1乘用车", 200.00);
		Column column11 = new Column("乘用车", "2乘用车", 200.00);
		Column column111 = new Column("乘用车", "2乘用车", 200.00);
		Column column12 = new Column("乘用车", "3乘用车", 200.00);
		Column column13 = new Column("乘用车", "4乘用车", 200.00);
		Column column14 = new Column("乘用车", "5乘用车", 200.00);
		Column column15 = new Column("乘用车", "6乘用车", 200.00);
		Column column151 = new Column("乘用车", "6乘用车", 200.00);
		Column column2 = new Column("商用车", "1商用车", 200.00);
		Column column21 = new Column("商用车", "2商用车", 200.00);
		Column column211 = new Column("商用车", "2商用车", 200.00);
		Column column22 = new Column("商用车", "3商用车", 200.00);
		Column column23 = new Column("商用车", "4商用车", 200.00);
		Column column24 = new Column("商用车", "5商用车", 200.00);
		Column column25 = new Column("商用车", "6商用车", 200.00);
		Column column3 = new Column("客车", "1客车", 200.00);
		Column column31 = new Column("客车", "2客车", 200.00);
		Column column32 = new Column("客车", "3客车", 200.00);
		Column column33 = new Column("客车", "4客车", 200.00);
		Column column34 = new Column("客车", "5客车", 200.00);
		Column column35 = new Column("客车", "6客车", 200.00);
		list.add(column1);
		list.add(column11);
		list.add(column111);
		list.add(column12);
		list.add(column13);
		list.add(column14);
		list.add(column15);
		list.add(column151);
		list.add(column2);
		list.add(column21);
		list.add(column211);
		list.add(column22);
		list.add(column23);
		list.add(column24);
		list.add(column25);
		list.add(column3);
		list.add(column31);
		list.add(column32);
		list.add(column33);
		list.add(column34);
		list.add(column35);

		List<KvPojo> collect = list.stream()
				.collect(Collectors.groupingBy(Column::getColumn1))
				.entrySet()
				.stream()
				.map(entry -> {
					String k = entry.getKey();
					double v = entry.getValue()
							.stream()
							.mapToDouble(Column::getColumn3)
							.sum();
					return new KvPojo(k, v);
				})
				.collect(Collectors.toList());

		List<Map<String, List<KvPojo>>> mapList = list.stream()
				.collect(Collectors.groupingBy(Column::getColumn1, Collectors.groupingBy(Column::getColumn2)))
				.entrySet()
				.stream()
				.map(entry -> {
					// 第取出一级分组
					String k1 = entry.getKey();
					// 二级分组的数据
					Map<String, List<Column>> v1 = entry.getValue();

					List<KvPojo> collect1 = v1.entrySet()
							.stream()
							.map(entry2 -> {
								String k2 = entry2.getKey();
								double v2 = entry2.getValue()
										.stream()
										.mapToDouble(Column::getColumn3)
										.sum();
								return new KvPojo(k2, v2);
							})
							.collect(Collectors.toList());

					Map<String, List<KvPojo>> listList = new LinkedHashMap<>();
					listList.put(k1, collect1);
					return listList;
				})
				.collect(Collectors.toList());

		String s = JSON.toJSONString(mapList);
		System.out.println(s);

		double sum = list.stream()
				.mapToDouble(Column::getColumn3)
				.sum();
		System.out.println(sum);
	}

	@Test
	public void testC() {
		List<String> listS = new ArrayList<>();
		listS.add("00:00");
		listS.add("00:30");
		listS.add("12:00");
		listS.add("8:30");
		listS.add("23:00");
		listS.stream()
				.sorted(Comparator.comparingInt(r -> Integer.valueOf(r.replace(":", ""))))
				.forEach(System.out::println);

		listS = new ArrayList<>();
		listS.add("2019（1-2月）");
		listS.add("2019（1-3月）");
		listS.add("2019（1-4月）");
		listS.add("2019（1-5月）");
		listS.add("2017");
		listS.add("2018");

		listS.stream()
				.sorted()
				.forEach(System.out::println);


	}

	/**
	 * list通过流转map,如果有相同的映射的话会报错，这时候我们要指定第三方策略
	 */
	@Test
	public void testCollectorsToMap() {
		people.stream()
				.collect(Collectors.toMap(Person::getFirstName
						, Person::getLastName
						, (existing, replacement) -> existing)
				)
				.entrySet()
				.forEach(System.out::println);
		System.out.println("*******************");
		people.stream()
				.collect(
						Collectors.toMap(Person::getFirstName
						, Person::getLastName
						, (o1, o2) -> o1
						, ConcurrentHashMap::new)
				)
				.entrySet()
				.forEach(System.out::println);
	}
}

