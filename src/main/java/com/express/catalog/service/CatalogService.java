package com.express.catalog.service;

import com.express.catalog.dto.InventoryDto;
import com.express.catalog.dto.ItemDetailDto;
import com.express.catalog.model.Product;
import com.express.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final InventoryServiceProperties inventoryServiceProperties;

    public CatalogService(ProductRepository productRepository, RestTemplate restTemplate,
                          InventoryServiceProperties inventoryServiceProperties) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.inventoryServiceProperties = inventoryServiceProperties;
    }

    public List<ItemDetailDto> getAllItemsSortedByScore() {
        return productRepository.findAll().stream()
                .map(this::enrichAndCalculateScore)
                .sorted(Comparator.comparingDouble((ItemDetailDto item) -> item.getScore() == null ? Double.NEGATIVE_INFINITY : item.getScore()).reversed())
                .collect(Collectors.toList());
    }

    public ItemDetailDto getItemById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
        return enrichAndCalculateScore(product);
    }

    private ItemDetailDto enrichAndCalculateScore(Product product) {
        InventoryDto inventory = fetchInventory(product.getId());

        ItemDetailDto dto = new ItemDetailDto();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());
        dto.setPrice(product.getPrice());
        dto.setRating(product.getRating());

        dto.setStock(inventory != null && inventory.getStock() != null ? inventory.getStock() : 0);
        dto.setStatus(inventory != null && inventory.getStatus() != null ? inventory.getStatus() : "UNKNOWN");

        dto.setScore(calculateScore(dto.getRating(), dto.getStock(), dto.getPrice()));
        return dto;
    }

    private InventoryDto fetchInventory(Long itemId) {
        try {
                return restTemplate.getForObject(inventoryServiceProperties.url() + "/api/inventory/" + itemId,
                    InventoryDto.class);
        } catch (Exception e) {
            InventoryDto fallback = new InventoryDto();
            fallback.setItemId(itemId);
            fallback.setStock(0);
            fallback.setStatus("UNAVAILABLE");
            return fallback;
        }
    }

    public double calculateScore(Double rating, Integer stock, Double price) {
        double r = (rating == null || rating < 0) ? 0.0 : rating;
        int s = (stock == null || stock < 0) ? 0 : stock;
        double p = (price == null || price <= 0) ? 1.0 : price;

        return (r * Math.log(s + 1)) / Math.max(p, 1.0);
    }
}