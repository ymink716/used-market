package com.ymink716.used_market.repository;

import com.ymink716.used_market.domain.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRequestRepository
    extends JpaRepository<TradeRequest, Long> {

    boolean existsByItemIdAndRequesterId(
        Long itemId,
        Long requesterId
    );
}