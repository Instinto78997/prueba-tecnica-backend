package com.express.catalog.dto;

import lombok.Data;

@Data
public class InventoryDto {
    private Long itemId;
    private Integer stock;
    private String status;
}