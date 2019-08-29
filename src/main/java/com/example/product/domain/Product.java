package com.example.product.domain;

import com.example.product.common.AbstractEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;

/**
 * @author mukibulØ
 * @since 23/08/19
 */

@Slf4j
@Entity
@Getter
public class Product extends AbstractEntity {

    private String name;
   // It can have other fields too. Just omitting for the sake of simplicity

    private Double price;


    public Product(String name,Double price){
        this.name=name;
        this.price=price;
    }
    /**
     * Default constructor
     */
    protected Product(){
        this(null,null);
    }
}
