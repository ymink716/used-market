package com.ymink716.used_market.controller;

import com.ymink716.used_market.dto.TradeRequestResponse;
import com.ymink716.used_market.dto.UpdateTradeRequestStatusRequest;
import com.ymink716.used_market.service.TradeRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TradeRequestController {

    private final TradeRequestService tradeRequestService;

    @PostMapping("/api/items/{itemId}/trade-requests")
    public ResponseEntity<TradeRequestResponse> create(@PathVariable Long itemId, Authentication authentication) {

        TradeRequestResponse response = tradeRequestService.create(
            itemId,
            authentication.getName()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping("/api/items/{itemId}/trade-requests")
    public ResponseEntity<List<TradeRequestResponse>> getTradeRequests(
        @PathVariable Long itemId,
        Authentication authentication
    ) {

        List<TradeRequestResponse> responses =
            tradeRequestService.findAllByItem(
                itemId,
                authentication.getName()
            );

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/api/trade-requests/{tradeRequestId}")
    public ResponseEntity<TradeRequestResponse> updateStatus(
        @PathVariable Long tradeRequestId,
        @Valid @RequestBody UpdateTradeRequestStatusRequest request,
        Authentication authentication
    ) {

        TradeRequestResponse response =
            tradeRequestService.updateStatus(
                tradeRequestId,
                request.getStatus(),
                authentication.getName()
            );

        return ResponseEntity.ok(response);
    }
}