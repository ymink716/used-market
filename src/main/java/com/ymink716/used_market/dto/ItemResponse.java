package com.ymink716.used_market.dto;

import com.ymink716.used_market.domain.Item;
import com.ymink716.used_market.domain.ItemStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Integer price;
    private final ItemStatus itemStatus;

    private final Long userId;
    private final String userNickname;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.price = item.getPrice();
        this.itemStatus = item.getItemStatus();

        this.userId = item.getUser().getId();
        this.userNickname = item.getUser().getNickname();

        this.createdAt = item.getCreatedAt();
        this.updatedAt = item.getUpdatedAt();
    }
}