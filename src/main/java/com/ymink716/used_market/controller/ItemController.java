package com.ymink716.used_market.controller;

import com.ymink716.used_market.dto.AddItemRequest;
import com.ymink716.used_market.dto.ItemResponse;
import com.ymink716.used_market.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponse> addItem(
        @Valid @RequestBody AddItemRequest request,
        Authentication authentication
    ) {

        ItemResponse response = itemService.addItem(request, authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long itemId) {

        ItemResponse response = itemService.findById(itemId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(
        @PathVariable Long itemId,
        @Valid @RequestBody AddItemRequest request,
        Authentication authentication
    ) {

        ItemResponse response = itemService.updateItem(itemId, request, authentication.getName());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(
        @PathVariable Long itemId,
        Authentication authentication
    ) {

        itemService.deleteItem(itemId, authentication.getName());

        return ResponseEntity.noContent().build();
    }
}