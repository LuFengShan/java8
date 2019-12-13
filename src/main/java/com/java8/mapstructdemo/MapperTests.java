package com.java8.mapstructdemo;


import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class MapperTests {

	private ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

	@Test
	public void mapperTests() {

		assertNotNull(mapper);

		Product api = new Product(1, "n", 1, "sa");

		ProductEntity entity = mapper.apiToEntity(api);
		System.err.println(entity.toString());
		assertEquals(api.getProductId(), entity.getProductId());
		assertEquals(api.getProductId(), entity.getProductId());
		assertEquals(api.getName(), entity.getName());
		assertEquals(api.getWeight(), entity.getWeight());

		Product api2 = mapper.entityToApi(entity);
		System.err.println(api2);
		assertEquals(api.getProductId(), api2.getProductId());
		assertEquals(api.getProductId(), api2.getProductId());
		assertEquals(api.getName(), api2.getName());
		assertEquals(api.getWeight(), api2.getWeight());
		assertNull(api2.getServiceAddress());
	}

	@Test
	public void test2() {
		System.out.println(Person.Builder.class.toString());
	}
}
