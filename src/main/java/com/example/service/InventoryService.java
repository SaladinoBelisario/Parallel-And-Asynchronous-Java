package com.example.service;

import com.example.domain.Inventory;
import com.example.domain.ProductOption;

import java.util.concurrent.CompletableFuture;

import static com.example.util.CommonUtil.delay;

public class InventoryService {
    public Inventory addInventory(ProductOption productOption) {
        delay(500);
        return Inventory.builder()
                .count(2).build();

    }

    public CompletableFuture<Inventory> addInventory_CF(ProductOption productOption) {

        return CompletableFuture.supplyAsync(() -> {
            delay(500);
            return Inventory.builder()
                    .count(2).build();
        });

    }

    public Inventory retrieveInventory(ProductOption productOption) {
        delay(500);
        return Inventory.builder()
                .count(2).build();

    }
}