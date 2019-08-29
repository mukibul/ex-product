package com.example.product.domain.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author mukibul
 * @since 23/08/19
 */
public interface ProductEventSource {
    String PRODUCT_PUBLISHER_EVENTS = "productEventsPublisherChannel";

    String PRODUCT_ADDED_PUBLISHER_EVENTS = "productAddedEventsPublisherChannel";

    @Output
    MessageChannel productEventsPublisherChannel();

    @Output
    MessageChannel productAddedEventsPublisherChannel();
}
