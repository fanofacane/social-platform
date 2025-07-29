package com.example.in_sky1.service;

import com.example.in_sky1.dto.PostDTO;
import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.CardDetail;

import java.util.List;
import java.util.Map;

public interface CardService {
    Map<String, Object> queryMoreCard(Integer userId, String fileType, Integer cursor, int size);

    CardDetail getCardById(Integer id,Integer userId);

    List<Card> getCardByKey(String key,Integer userId);

    List<Card> queryCardById(Integer id);

    String insertPost(PostDTO postDTO);
}
