package com.express.inventory.controller;

import com.express.inventory.model.InventoryItem;
import com.express.inventory.repository.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    private final InventoryRepository repository;

    public InventoryController(InventoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<InventoryItem> getInventoryByItemId(@PathVariable("itemId") Long itemId) {
        return repository.findById(itemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}