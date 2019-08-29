package com.example.product.domain.messaging;

import com.example.event.ProductAddedOrUpdated;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import com.example.event.ProductAddToCartEvent;
/**
 * @author mukibul
 * @since 23/08/19
 *
 * External event publisher for {@link com.example.product.domain} events.
 */

@MessagingGateway
public interface ProductEventPublisher {

    @Gateway(requestChannel = ProductEventSource.PRODUCT_PUBLISHER_EVENTS)
    void publish(ProductAddToCartEvent productAddToCardEvent);

    @Gateway(requestChannel = ProductEventSource.PRODUCT_ADDED_PUBLISHER_EVENTS)
    void publish(ProductAddedOrUpdated productAddedOrUpdated);
}
