package com.ymink716.used_market.service;

import com.ymink716.used_market.common.exception.ForbiddenException;
import com.ymink716.used_market.common.exception.ItemNotFoundException;
import com.ymink716.used_market.domain.Item;
import com.ymink716.used_market.domain.User;
import com.ymink716.used_market.dto.AddItemRequest;
import com.ymink716.used_market.dto.ItemResponse;
import com.ymink716.used_market.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Transactional
    public ItemResponse addItem(AddItemRequest request, String email) {

        User user = userService.findByEmail(email);

        Item item = Item.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .price(request.getPrice())
            .user(user)
            .build();

        Item savedItem = itemRepository.save(item);

        return new ItemResponse(savedItem);
    }

    @Transactional(readOnly = true)
    public ItemResponse findById(Long itemId) {

        Item item = findItem(itemId);

        return new ItemResponse(item);
    }

    @Transactional
    public ItemResponse updateItem(Long itemId, AddItemRequest addItemRequest, String email) {

        Item item = findItem(itemId);
        validateAuthor(item, email);

        item.update(
            addItemRequest.getTitle(),
            addItemRequest.getContent(),
            addItemRequest.getPrice()
        );

        return new ItemResponse(item);
    }

    @Transactional
    public void deleteItem(Long itemId, String email) {

        Item item = findItem(itemId);
        validateAuthor(item, email);

        itemRepository.delete(item);
    }

    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException("존재하지 않는 상품입니다."));
    }

    private void validateAuthor(Item item, String email) {
        if (!item.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("상품을 수정하거나 삭제할 권한이 없습니다.");
        }
    }
}