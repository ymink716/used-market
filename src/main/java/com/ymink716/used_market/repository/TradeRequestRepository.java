package com.ymink716.used_market.repository;

import com.ymink716.used_market.domain.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRequestRepository
    extends JpaRepository<TradeRequest, Long> {

    boolean existsByItemIdAndRequesterId(
        Long itemId,
        Long requesterId
    );

    List<TradeRequest> findAllByItemId(Long itemId);
}