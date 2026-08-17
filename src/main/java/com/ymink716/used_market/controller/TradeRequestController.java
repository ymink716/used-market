package com.ymink716.used_market.controller;

import com.ymink716.used_market.dto.TradeRequestResponse;
import com.ymink716.used_market.service.TradeRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TradeRequestController {

    private final TradeRequestService tradeRequestService;

    @PostMapping("/api/items/{itemId}/trade-requests")
    public ResponseEntity<TradeRequestResponse> create(
        @PathVariable Long itemId,
        Authentication authentication
    ) {

        TradeRequestResponse response = tradeRequestService.create(
            itemId, authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}