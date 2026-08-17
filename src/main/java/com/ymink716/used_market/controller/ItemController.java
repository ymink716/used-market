package com.ymink716.used_market.controller;

import com.ymink716.used_market.dto.AddItemRequest;
import com.ymink716.used_market.dto.ItemResponse;
import com.ymink716.used_market.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        String email = authentication.getName();

        ItemResponse response =
            itemService.addItem(request, email);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }
}
