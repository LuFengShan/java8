package com.java8.collection;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

public class CollectorDemo {

	@Test
	public void testTreeSet() {

		TreeSet<OfflineVehicle> set = new TreeSet<>(Comparator.comparingInt(o -> o.getVin().hashCode()));
		long start = System.currentTimeMillis();
		System.out.println("TreeSet测试200000万数据耗时：");
		String uuid = UUID.randomUUID().toString();
		set.add(OfflineVehicle.builder().vin(uuid).build());
		IntStream.range(1, 199999)
				.forEach(i -> set.add(OfflineVehicle.builder().vin(UUID.randomUUID().toString()).build()));
		long end = System.currentTimeMillis();
		System.out.println("TreeSet插入200000万数据耗时：" + (end - start));
		System.out.println(set.stream()
				.filter(offlineVehicle -> Objects.equals(uuid, offlineVehicle.getVin()))
				.findFirst()
				.get()
				.toString()
		);

		for (OfflineVehicle o1 : set) {
			if (Objects.equals(o1.getVin(), uuid)) {
				System.out.println(o1.toString());
			}
		}
		long end2 = System.currentTimeMillis();
		System.out.println("TreeSet查询第120000万数据耗时：" + (end2 - end));
		System.out.println("ArrayList测试200000万数据耗时：");
		List<OfflineVehicle> list = new ArrayList<>(300000);
		start = System.currentTimeMillis();
		list.add(OfflineVehicle.builder().vin(uuid).build());
		IntStream.range(1, 199999)
				.forEach(i -> list.add(OfflineVehicle.builder().vin(UUID.randomUUID().toString()).build()));
		end = System.currentTimeMillis();
		System.out.println("ArrayList插入200000万数据耗时：" + (end - start));
		System.out.println(list.stream()
				.filter(offlineVehicle -> Objects.equals(uuid, offlineVehicle.getVin()))
				.findFirst()
				.get()
				.toString()
		);
		end2 = System.currentTimeMillis();
		System.out.println("ArrayList查询第120000万数据耗时：" + (end2 - end));
	}
}
