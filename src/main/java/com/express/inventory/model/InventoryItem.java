package com.express.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "inventory")
@Data
public class InventoryItem {

    @Id
    @Column(name = "item_id")
    private Long itemId;
    
    private Integer stock;
    private String status;
}