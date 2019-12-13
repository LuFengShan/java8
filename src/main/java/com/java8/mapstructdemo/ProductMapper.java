package com.java8.mapstructdemo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface ProductMapper {
	@Mappings({
			@Mapping(target = "serviceAddress", ignore = true)
	})
	Product entityToApi(ProductEntity entity);

	@Mappings({
			@Mapping(target = "id", ignore = true),
			@Mapping(target = "version", ignore = true)
	})
	ProductEntity apiToEntity(Product api);
}
