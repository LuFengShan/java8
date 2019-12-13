package com.java8.mapstructdemo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {
	private String id;

	private Integer version;

	private int productId;

	private String name;
	private int weight;
}
