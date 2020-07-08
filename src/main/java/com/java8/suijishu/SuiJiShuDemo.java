package com.java8.suijishu;

import cn.bitnei.gpsarea.dto.Coordinate;
import cn.bitnei.gpsarea.handle.AreaFenceHandler;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SuiJiShuDemo {
	@Test
	public void threadLoaclRandom() throws IOException {
		AreaFenceHandler handler = new AreaFenceHandler();
		List<String> list;
		Coordinate coordinate;
		long i = 0;
		List<LonLat> lonLatList = new ArrayList<>(15000000);
		while (true) {
			coordinate = creadLonLat();
			list = handler.areaIds(coordinate);
			if (Objects.nonNull(list) && Objects.equals(list.size(), 3)) {
				lonLatList.add(LonLat.builder()
						.lon(coordinate.x)
						.lat(coordinate.y)
						.province(list.get(0))
						.city(list.get(1))
						.district(list.get(2))
						.build());
				i++;
			}
			if (i == 10000000) {
				break;
			}
		}
//		Map<String, Long> district = lonLatList.parallelStream()
//				.collect(Collectors.groupingBy(LonLat::getDistrict, Collectors.counting()));
//		district.entrySet()
//				.forEach(entry -> System.out.println(entry.getKey() + ":" + entry.getValue()));
//		System.out.println("--------------------");
//		Map<String, Long> city = lonLatList.parallelStream()
//				.collect(Collectors.groupingBy(LonLat::getCity, Collectors.counting()));
//		city.entrySet()
//				.forEach(entry -> System.out.println(entry.getKey() + ":" + entry.getValue()));
//		System.out.println("--------------------");
//
//		Map<String, Long> province = lonLatList.parallelStream()
//				.collect(Collectors.groupingBy(LonLat::getProvince, Collectors.counting()));
//		province.entrySet()
//				.forEach(entry -> System.out.println(entry.getKey() + ":" + entry.getValue()));
//		System.out.println("--------------------");
//		Map<String, Map<String, Map<String, Long>>> collect = lonLatList.parallelStream()
//				.collect(Collectors.groupingBy(LonLat::getProvince,
//						Collectors.groupingBy(LonLat::getCity,
//								Collectors.groupingBy(LonLat::getDistrict, Collectors.counting()))));
//		collect.entrySet()
//				.forEach(k1 -> k1.getValue()
//						.entrySet()
//						.forEach(k2 -> k2.getValue()
//								.entrySet()
//								.forEach(k3 ->
//										System.out.println(k1.getKey() + ":" + k2.getKey() + ":" + k3.getKey() + ":" + k3.getValue())
//								)
//						)
//				);
//		Map<String, Map<String, List<String>>> collect1 = lonLatList.parallelStream()
//				.collect(Collectors.groupingBy(LonLat::getProvince,
//						Collectors.groupingBy(LonLat::getCity,
//								Collectors.mapping(LonLat::getDistrict, Collectors.toList()))));
//		String string = JSON.toJSONString(collect1);
//		System.out.println(string);

//		List<String> collect2 = lonLatList.parallelStream()
//				.map(lonLat -> String.join(",",
//						String.valueOf(xy(String.valueOf(lonLat.getLon()))),
//						String.valueOf(xy(String.valueOf(lonLat.getLat()))),
//						UUID.randomUUID().toString().substring(0, 10)))
//				.collect(Collectors.toList());
//		ResultBean<List<String>> resultBean = new ResultBean<>();
//		resultBean.setCode("ok");
//		resultBean.setData(collect2);
//		resultBean.setMsg("");
//		String string2 = JSON.toJSONString(resultBean);
//		System.out.println(string2);
		
		List<LonLat3> collect3 = lonLatList.parallelStream()
				.map(lonlat -> LonLat3.builder()
						.lon(xy(String.valueOf(lonlat.getLon())))
						.lat(xy(String.valueOf(lonlat.getLat())))
						.mark(UUID.randomUUID().toString()
								.replace("-", "")
								.substring(0, 10)
						)
						.build())
				.collect(Collectors.toList());
		ResultBean<List<LonLat3>> resultBean1 = new ResultBean<>();
		resultBean1.setCode("ok");
		resultBean1.setData(collect3);
		resultBean1.setMsg("");
		String string3 = JSON.toJSONString(resultBean1);
		// System.out.println(string3);
		Path pathWrite = Paths.get("d:/xianshukeji.json");
		BufferedWriter writer = Files.newBufferedWriter(pathWrite);
		writer.write(string3, 0, string3.length());
		writer.flush();
		writer.close();
		System.out.println("over");
	}
	
	private Coordinate creadLonLat() {
		return new Coordinate(ThreadLocalRandom.current().nextDouble(72.005, 137.8347),
				ThreadLocalRandom.current().nextDouble(0.8293, 55.8271));
	}
	
	public static double xy(String str) {
		String[] split = str.split("\\.");
		if (split[1].length() > 6) {
			return Double.parseDouble(split[0] + "." + split[1].substring(0, 6));
		}
		return Double.parseDouble(str);
	}
}

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
class LonLat {
	private double lon;
	private double lat;
	private String district;
	private String city;
	private String province;
}

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
class LonLat3 {
	private double lon;
	private double lat;
	private String mark;
}