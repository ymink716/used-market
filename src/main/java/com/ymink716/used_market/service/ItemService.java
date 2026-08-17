package com.ymink716.used_market.service;

import com.ymink716.used_market.domain.Item;
import com.ymink716.used_market.domain.User;
import com.ymink716.used_market.dto.AddItemRequest;
import com.ymink716.used_market.dto.ItemResponse;
import com.ymink716.used_market.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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

}
