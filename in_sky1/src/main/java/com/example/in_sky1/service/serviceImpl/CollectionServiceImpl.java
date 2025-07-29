package com.example.in_sky1.service.serviceImpl;

import com.example.in_sky1.dto.CollectionDTO;
import com.example.in_sky1.mapper.CollectionsMapper;
import com.example.in_sky1.mapper.LikeMapper;
import com.example.in_sky1.mapper.PostMapper;
import com.example.in_sky1.mapper.UserMapper;
import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.Like;
import com.example.in_sky1.pojo.User;
import com.example.in_sky1.service.CollectionService;
import com.example.in_sky1.utils.CurrentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    private CollectionsMapper collectionsMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public void toggleCollection(CollectionDTO collectionDTO) {
        boolean exists = collectionsMapper.checkCollectionExists(collectionDTO.getUserId(), collectionDTO.getPostId());
        String key="cardCollectedCache::"+collectionDTO.getPostId();
        if (exists) {
            collectionsMapper.deleteCollection(collectionDTO.getUserId(), collectionDTO.getPostId());
            updateCollectionCount(collectionDTO.getPostId(), -1);
            redisTemplate.opsForSet().remove(key, collectionDTO.getUserId().toString());
        } else {
            collectionDTO.setCreateTime(LocalDateTime.now());
            collectionsMapper.insertCollection(collectionDTO);
            updateCollectionCount(collectionDTO.getPostId(), 1);
            redisTemplate.opsForSet().add(key, collectionDTO.getUserId().toString());
        }
    }

    @Override
    public List<Card> getCollectionListByUserId() {
        Integer userId= CurrentHolder.getCurrentId();
        List<Card> cardList=collectionsMapper.getCollectionListByUserId(userId);
        for (Card card : cardList) {
            Boolean exist=likeMapper.checkLikeExists(userId,card.getId(),null);
            card.setIsLike(exist);
            User user=userMapper.selectUserById(card.getAuthorId());
            card.setPicture(user.getPicture());
            card.setAuthor(user.getUsername());
            card.setIsCollection(true);
        }
        return cardList;
    }

    private void updateCollectionCount(Integer postId, int count) {
        if (postId != null) {
            postMapper.updatePostCollection(postId, count);
        }
    }
}
