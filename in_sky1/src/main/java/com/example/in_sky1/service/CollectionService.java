package com.example.in_sky1.service;

import com.example.in_sky1.dto.CollectionDTO;
import com.example.in_sky1.pojo.Card;

import java.util.List;

public interface CollectionService {
    void toggleCollection(CollectionDTO collectionDTO);

    List<Card> getCollectionListByUserId();
}
