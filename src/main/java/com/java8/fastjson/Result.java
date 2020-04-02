package com.java8.fastjson;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
	
	/**
	 * 接口返回状态
	 */
	private int code;
	/**
	 * 接口说明
	 */
	private String msg;
	/**
	 * 接口封装的数据
	 */
	private List<GpsDemo> data;
	
	
	public static void main(String[] args) {
		
		List<GpsDemo> list = new ArrayList<>();
		list.add(GpsDemo.builder().wgLat(40D).wgLon(80D).build());
		list.add(GpsDemo.builder().wgLat(50D).wgLon(70D).build());
		list.add(GpsDemo.builder().wgLat(60D).wgLon(40D).build());
		
		Result result = new Result();
		result.setCode(200);
		result.setMsg("自己定义的信息");
		result.setData(list);
		String json = JSON.toJSONString(result);
		// {"code":200,"data":[{"wgLat":40.0,"wgLon":80.0},{"wgLat":50.0,"wgLon":70.0},{"wgLat":60.0,"wgLon":40.0}],"msg":"自己定义的信息"}
		System.out.println(json);
	}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class GpsDemo implements Serializable {
	private double wgLat;
	private double wgLon;
}