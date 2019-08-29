package com.example.product.controller;

import com.example.product.domain.Product;
import com.example.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author mukibul
 * @since 23/08/19
 */
@Slf4j
@BasePathAwareController
public class ProductController {


    @Autowired
    protected Repositories repositories;

    @Autowired
    private ProductService productService;

    @PutMapping(value = "/products/{id}/addToCart/{cart_id}")
    @ResponseBody
    public ResponseEntity<?> addToCart(@PathVariable(value = "id") Product product,@PathVariable(value = "cart_id") Long cartId) {

        productService.addToCart(product,cartId);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @PostMapping(value = "/products")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestBody Product product) {

        product=productService.save(product);
        PersistentEntityResource productResource = PersistentEntityResource.build(product,repositories.getPersistentEntity(Product.class)).build();
        return new ResponseEntity<Object>(productResource,HttpStatus.CREATED);
    }


}
