package com.ymink716.used_market.dto;

import com.ymink716.used_market.domain.TradeRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTradeRequestStatusRequest {

    @NotNull
    private TradeRequestStatus status;
}
