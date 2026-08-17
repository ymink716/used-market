package com.ymink716.used_market.dto;

import com.ymink716.used_market.domain.TradeRequest;
import com.ymink716.used_market.domain.TradeRequestStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TradeRequestResponse {

    private final Long id;
    private final Long itemId;
    private final Long requesterId;
    private final String requesterNickname;
    private final TradeRequestStatus status;
    private final LocalDateTime createdAt;

    public TradeRequestResponse(TradeRequest tradeRequest) {
        this.id = tradeRequest.getId();
        this.itemId = tradeRequest.getItem().getId();
        this.requesterId = tradeRequest.getRequester().getId();
        this.requesterNickname = tradeRequest.getRequester().getNickname();
        this.status = tradeRequest.getStatus();
        this.createdAt = tradeRequest.getCreatedAt();
    }
}
