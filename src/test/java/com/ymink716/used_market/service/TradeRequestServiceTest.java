package com.ymink716.used_market.service;

import com.ymink716.used_market.domain.Item;
import com.ymink716.used_market.domain.ItemStatus;
import com.ymink716.used_market.domain.TradeRequest;
import com.ymink716.used_market.domain.TradeRequestStatus;
import com.ymink716.used_market.domain.User;
import com.ymink716.used_market.dto.TradeRequestResponse;
import com.ymink716.used_market.repository.ItemRepository;
import com.ymink716.used_market.repository.TradeRequestRepository;
import com.ymink716.used_market.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TradeRequestServiceTest {

    @Autowired
    private TradeRequestService tradeRequestService;

    @Autowired
    private TradeRequestRepository tradeRequestRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 거래요청을_수락하면_다른_대기요청은_거절되고_상품은_예약된다() {

        // given
        User seller = userRepository.save(
            User.builder()
                .email("seller@test.com")
                .password(passwordEncoder.encode("12345678"))
                .nickname("판매자")
                .build()
        );

        User buyer1 = userRepository.save(
            User.builder()
                .email("buyer1@test.com")
                .password(passwordEncoder.encode("12345678"))
                .nickname("구매자1")
                .build()
        );

        User buyer2 = userRepository.save(
            User.builder()
                .email("buyer2@test.com")
                .password(passwordEncoder.encode("12345678"))
                .nickname("구매자2")
                .build()
        );

        Item item = itemRepository.save(
            Item.builder()
                .title("맥북 판매합니다")
                .content("상태 좋습니다.")
                .price(500000)
                .user(seller)
                .build()
        );

        TradeRequestResponse buyer1Request =
            tradeRequestService.create(
                item.getId(),
                buyer1.getEmail()
            );

        TradeRequestResponse buyer2Request =
            tradeRequestService.create(
                item.getId(),
                buyer2.getEmail()
            );

        // 요청 수락 전 상태 확인
        assertThat(buyer1Request.getStatus())
            .isEqualTo(TradeRequestStatus.PENDING);

        assertThat(buyer2Request.getStatus())
            .isEqualTo(TradeRequestStatus.PENDING);

        assertThat(item.getItemStatus())
            .isEqualTo(ItemStatus.SELLING);


        // when
        tradeRequestService.updateStatus(
            buyer1Request.getId(),
            TradeRequestStatus.ACCEPTED,
            seller.getEmail()
        );


        // then
        TradeRequest acceptedRequest = tradeRequestRepository.findById(buyer1Request.getId()).orElseThrow();
        TradeRequest rejectedRequest = tradeRequestRepository.findById(buyer2Request.getId()).orElseThrow();
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();

        assertThat(acceptedRequest.getStatus()).isEqualTo(TradeRequestStatus.ACCEPTED);
        assertThat(rejectedRequest.getStatus()).isEqualTo(TradeRequestStatus.REJECTED);
        assertThat(updatedItem.getItemStatus()).isEqualTo(ItemStatus.RESERVED);
    }
}