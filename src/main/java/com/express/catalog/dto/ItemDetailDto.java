package com.express.catalog.dto;

import lombok.Data;

@Data
public class ItemDetailDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Double price;
    private Double rating;
    private Integer stock;
    private String status;
    private Double score;
}