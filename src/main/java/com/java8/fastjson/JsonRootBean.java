/**
 * Copyright 2019 bejson.com
 */
package com.java8.fastjson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JsonRootBean {
	private List<Dots> dots;
}