package com.ymink716.used_market.service;

import com.ymink716.used_market.common.exception.DuplicateTradeRequestException;
import com.ymink716.used_market.common.exception.ForbiddenException;
import com.ymink716.used_market.common.exception.InvalidTradeRequestException;
import com.ymink716.used_market.common.exception.ItemNotFoundException;
import com.ymink716.used_market.domain.Item;
import com.ymink716.used_market.domain.ItemStatus;
import com.ymink716.used_market.domain.TradeRequest;
import com.ymink716.used_market.domain.User;
import com.ymink716.used_market.dto.TradeRequestResponse;
import com.ymink716.used_market.repository.ItemRepository;
import com.ymink716.used_market.repository.TradeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeRequestService {

    private final TradeRequestRepository tradeRequestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Transactional
    public TradeRequestResponse create(Long itemId, String email) {

        User requester = userService.findByEmail(email);

        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException("존재하지 않는 상품입니다."));

        if (item.getUser().getId().equals(requester.getId())) {
            throw new ForbiddenException("자신의 상품에는 거래 요청을 할 수 없습니다.");
        }

        if (item.getItemStatus() != ItemStatus.SELLING) {
            throw new InvalidTradeRequestException("판매 중인 상품에만 거래 요청을 할 수 있습니다.");
        }

        if (tradeRequestRepository.existsByItemIdAndRequesterId(itemId, requester.getId())) {
            throw new DuplicateTradeRequestException("이미 거래 요청한 상품입니다.");
        }

        TradeRequest tradeRequest = TradeRequest.builder()
            .item(item)
            .requester(requester)
            .build();

        TradeRequest saved = tradeRequestRepository.save(tradeRequest);

        return new TradeRequestResponse(saved);
    }
}
