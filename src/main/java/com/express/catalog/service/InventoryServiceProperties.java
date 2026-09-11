package com.express.catalog.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "inventory.service")
public record InventoryServiceProperties(String url) {
    public InventoryServiceProperties {
        if (url == null || url.isBlank()) {
            url = "http://localhost:8082";
        }
    }
}