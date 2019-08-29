package com.example.product.config;

import com.example.product.domain.messaging.ProductEventSource;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;

/**
 * @author mukibul
 * @since 23/08/19
 */
@Configuration
@EnableBinding({ProductEventSource.class})
@IntegrationComponentScan(basePackages = "com.example")
public class IntegrationConfiguration {
}
