package com.express.catalog.controller;

import com.express.catalog.dto.ItemDetailDto;
import com.express.catalog.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDetailDto>> getItems() {
        return ResponseEntity.ok(catalogService.getAllItemsSortedByScore());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailDto> getItemById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(catalogService.getItemById(id));
    }
}