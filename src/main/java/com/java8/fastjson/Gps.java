package com.java8.fastjson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 充电热力图
 *
 * @author sgx
 * @version V1.1.0
 * @date 2019/08/06 18:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Gps implements Serializable {
	private double wgLat;
	private double wgLon;
}
