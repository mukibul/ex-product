package com.example.product.service;

import com.example.event.ProductAddToCartEvent;
import com.example.event.ProductAddedOrUpdated;
import com.example.product.domain.Product;
import com.example.product.domain.messaging.ProductEventPublisher;
import com.example.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mukibulØ
 * @since 23/08/19
 */

@Slf4j
@Service("productService")
public class ProductService {

//    @Autowired
//    ApplicationEventPublisher publisher;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductEventPublisher publisher;

    public void addToCart(Product product,Long cartId) {
        ProductAddToCartEvent productAddToCartEvent=new ProductAddToCartEvent();
        ProductAddToCartEvent.Product product1 = new ProductAddToCartEvent.Product();
        product1.setId(product.getId());
        product1.setName(product.getName());
        product1.setPrice(product.getPrice().toString());
        productAddToCartEvent.setProduct(product1);
        productAddToCartEvent.setCartId(cartId);
        publisher.publish(productAddToCartEvent);
    }

    public Product save(Product product) {
        product = productRepository.save(product);
        publisher.publish(new ProductAddedOrUpdated(product.getId(),product.getName(),product.getPrice()));
        return product;
    }
}
