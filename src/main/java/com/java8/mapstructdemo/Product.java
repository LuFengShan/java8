package com.java8.mapstructdemo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
	private int productId;
	private String name;
	private int weight;
	private String serviceAddress;
}
