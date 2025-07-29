package com.example.in_sky1.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.example.in_sky1.dto.PostDTO;
import com.example.in_sky1.mapper.*;
import com.example.in_sky1.pojo.*;
import com.example.in_sky1.service.CardService;
import com.example.in_sky1.service.CommentService;
import com.example.in_sky1.service.FollowService;
import com.example.in_sky1.service.UserService;
import com.example.in_sky1.utils.CurrentHolder;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private CollectionsMapper collectionsMapper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FollowService followService;
    @Override
    public Map<String, Object> queryMoreCard(Integer userId,String fileType,Integer cursor,int size) {
        Integer commentId=null;
        List<Card> cardList;
        if (cursor == null) {
            // 第一页查询
            cardList = cardMapper.queryMoreCard(size);
        } else {
            // 获取游标对应的视频的create_time
            PostDTO cursorPost = cardMapper.findById(cursor);
            if (cursorPost == null) {return null;}
            // 查询下一页
            cardList = cardMapper.findNextPage(cursor, cursorPost.getCreateTime(), size);
        }
        // 计算下一次游标
        Integer nextCursor = null;
        if (!cardList.isEmpty()) {
            nextCursor = cardList.get(cardList.size() - 1).getId();
        }
            for (Card card:cardList) {
                if(userId!=null){
                    boolean isFollowing = followService.checkIsFollowing(userId, card.getAuthorId());
                    boolean likedExists = likeMapper.checkLikeExists(userId, card.getId(), null);
                    boolean collectedExists = collectionsMapper.checkCollectionExists(userId, card.getId());
                    card.setIsLike(likedExists);
                    card.setIsCollection(collectedExists);
                    card.setIsFollow(isFollowing);
                }
                AuthorDetail authorDetail = userMapper.getUserById(card.getAuthorId());
                card.setAuthor(authorDetail.getUsername());
                card.setPicture(authorDetail.getPicture());
        }
        // 打乱列表
        Collections.shuffle(cardList);
        // 返回结果
        Map<String, Object> response = new HashMap<>();
            if(fileType==null){
                response.put("data", cardList);
                response.put("nextCursor", nextCursor);
                return response;
            }
        List<Card> media = cardList.stream().filter(c -> c.getFileType().startsWith(fileType)).collect(Collectors.toList());
        response.put("data", media);
        response.put("nextCursor", nextCursor);
        return response;
    }

    @Override
    public CardDetail getCardById(Integer id,Integer userId) {
        CardDetail cardDetail=cardMapper.getCardById(id);//根据id查找posts表
        String collectedKey="cardCollectedCache::"+id;
        AuthorDetail authorDetail=userMapper.getUserById(cardDetail.getAuthorId());//根据作者id查找用户
        cardDetail.setAuthorDetail(authorDetail);
        if(userId!=null){
            Boolean collectedExists=redisTemplate.opsForSet().isMember(collectedKey,userId.toString());
            if(collectedExists!=null&&!collectedExists){
                cardDetail.setIsCollection(collectedExists);
            }else {
                boolean b = collectionsMapper.checkCollectionExists(userId, id);
                cardDetail.setIsCollection(b);
            }
        }
        String comments = redisTemplate.opsForValue().get("cardCommentsCache::" + id);
        if(comments!=null&&!comments.isEmpty()){
            cardDetail.setCommentsList(JSON.parseArray(comments, Commentt.class));
        }else {
            cardDetail.setCommentsList(commentService.getCommentTree(id,userId));
        }
        return cardDetail;
    }

    @Override
    public List<Card> getCardByKey(String key,Integer userId) {
        Integer commentId=null;
        List<Card> cardList=cardMapper.getCardByKey(key);
        for (Card card:cardList) {
            Boolean likedExists = likeMapper.checkLikeExists(userId, card.getId(), commentId);
            Boolean collectionExists = collectionsMapper.checkCollectionExists(userId, card.getId());
            AuthorDetail authorDetail=userMapper.getUserById(card.getAuthorId());
            card.setIsLike(likedExists);
            card.setIsCollection(collectionExists);
            card.setPicture(authorDetail.getPicture());
            card.setAuthor(authorDetail.getUsername());
        }
        return cardList;
    }

    @Override
    public List<Card> queryCardById(Integer id) {
        List<Card> cardList=cardMapper.queryCardById(id);
        for (Card card:cardList) {
            boolean exist;
            exist = CurrentHolder.getCurrentId()==null?false: likeMapper.checkLikeExists(CurrentHolder.getCurrentId(),card.getId(),null);
            AuthorDetail authorDetail=userMapper.getUserById(card.getAuthorId());
            card.setAuthor(authorDetail.getUsername());
            card.setPicture(authorDetail.getPicture());
            card.setIsLike(exist);
        }
        return cardList;
    }

    @Override
    public String insertPost(PostDTO postDTO) {
        try {
            postDTO.setPictureUrl(postDTO.getPictureUrl());
            postDTO.setAuthorId(CurrentHolder.getCurrentId());
            postDTO.setCreateTime(LocalDateTime.now());
            postDTO.setUpdateTime(LocalDateTime.now());
            cardMapper.insert(postDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    return "success";
    }
}
