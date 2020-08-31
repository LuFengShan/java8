package com.java8.mapstructdemo;

import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-08-05T17:03:13+0800",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_152 (Oracle Corporation)"
)
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product entityToApi(ProductEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Product product = new Product();

        product.setProductId( entity.getProductId() );
        product.setName( entity.getName() );
        product.setWeight( entity.getWeight() );

        return product;
    }

    @Override
    public ProductEntity apiToEntity(Product api) {
        if ( api == null ) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();

        productEntity.setProductId( api.getProductId() );
        productEntity.setName( api.getName() );
        productEntity.setWeight( api.getWeight() );

        return productEntity;
    }
}