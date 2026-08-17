package com.ymink716.used_market.service;

import com.ymink716.used_market.common.exception.*;
import com.ymink716.used_market.domain.*;
import com.ymink716.used_market.dto.TradeRequestResponse;
import com.ymink716.used_market.repository.ItemRepository;
import com.ymink716.used_market.repository.TradeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeRequestService {

    private final TradeRequestRepository tradeRequestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Transactional
    public TradeRequestResponse create(Long itemId, String email) {

        User requester = userService.findByEmail(email);
        Item item = findItem(itemId);
        validateTradeRequest(item, requester);

        TradeRequest tradeRequest = TradeRequest.builder()
            .item(item)
            .requester(requester)
            .build();

        TradeRequest savedTradeRequest = tradeRequestRepository.save(tradeRequest);

        return new TradeRequestResponse(savedTradeRequest);
    }

    private void validateTradeRequest(Item item, User requester) {

        if (item.getUser().getId().equals(requester.getId())) {
            throw new ForbiddenException("자신의 상품에는 거래 요청을 할 수 없습니다.");
        }

        if (item.getItemStatus() != ItemStatus.SELLING) {
            throw new InvalidTradeRequestException("판매 중인 상품에만 거래 요청을 할 수 있습니다.");
        }

        if (tradeRequestRepository.existsByItemIdAndRequesterId(item.getId(), requester.getId())) {
            throw new DuplicateTradeRequestException("이미 거래 요청한 상품입니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<TradeRequestResponse> findAllByItem(Long itemId, String email) {

        Item item = findItem(itemId);
        validateSeller(item, email);

        return tradeRequestRepository.findAllByItemId(itemId)
            .stream()
            .map(TradeRequestResponse::new)
            .toList();
    }

    @Transactional
    public TradeRequestResponse updateStatus(
        Long tradeRequestId,
        TradeRequestStatus status,
        String email
    ) {

        TradeRequest tradeRequest = findTradeRequest(tradeRequestId);
        validateSeller(tradeRequest.getItem(), email);

        if (status == TradeRequestStatus.ACCEPTED) {
            tradeRequest.accept();
            tradeRequest.getItem().reserve();
        } else if (status == TradeRequestStatus.REJECTED) {
            tradeRequest.reject();
        } else {
            throw new InvalidTradeRequestException("거래 요청은 수락 또는 거절만 가능합니다.");
        }

        return new TradeRequestResponse(tradeRequest);
    }

    private Item findItem(Long itemId) {

        return itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException("존재하지 않는 상품입니다."));
    }

    private void validateSeller(Item item, String email) {

        if (!item.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("해당 상품의 거래 요청을 확인할 권한이 없습니다.");
        }
    }

    private TradeRequest findTradeRequest(Long tradeRequestId) {

        return tradeRequestRepository.findById(tradeRequestId)
            .orElseThrow(() -> new TradeRequestNotFoundException("존재하지 않는 거래 요청입니다."));
    }
}