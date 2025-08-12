package com.example.in_sky1.service.serviceImpl;

import com.example.in_sky1.mapper.*;
import com.example.in_sky1.pojo.*;
import com.example.in_sky1.service.FollowService;
import com.example.in_sky1.service.PostService;
import com.example.in_sky1.utils.CurrentHolder;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private FollowService followService;
    @Autowired
    private CollectionsMapper collectionsMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    // Redis键前缀，用于区分不同用户的收件箱
    private static final String FEED_INBOX_PREFIX = "feed:inbox:";

    public List<Post> getPostsByFollowed() {
        Integer currentUserId = CurrentHolder.getCurrentId();
        List<Integer> followedIds = userMapper.getFollowingIds(currentUserId);
        if (followedIds == null || followedIds.isEmpty()) {
            return null;
        }
        List<Post> posts = postMapper.getPostsByFollowed(followedIds);
        for (Post post : posts) {
            Integer commentId = null;
            boolean likedExists = likeMapper.checkLikeExists(currentUserId, post.getId(), commentId);
            boolean collectionExists = collectionsMapper.checkCollectionExists(currentUserId, post.getId());
            State state = new State();
            state.setIsFollow(true);
            state.setIsCollection(collectionExists);
            state.setIsLike(likedExists);
            User user = userMapper.selectUserById(post.getAuthorId());
            post.setUser(user);
            post.setState(state);
        }
        return posts;
    }
    @Override
    public Map<String, Object> getUserInboxVideos(Integer count, Long lastTimestamp) {
        Integer userId = CurrentHolder.getCurrentId();
        String inboxKey = FEED_INBOX_PREFIX + userId;
        List<Integer> videosIds = getUserInboxVideosIds(userId, count, lastTimestamp);
        Long lastTimestamp1 = getLastTimestamp(videosIds, inboxKey);
        List<Card> feedVideo = getFeedVideo(videosIds);
        Map<String, Object> result  = new HashMap<>();
        result.put("data", feedVideo);
        result.put("lastTimestamp", lastTimestamp1);
        return result;
    }

    public List<Integer> getUserInboxVideosIds(Integer userId, Integer count, Long lastTimestamp) {
        String inboxKey = FEED_INBOX_PREFIX + userId;
        Set<String> videoIdStrs;
        if (lastTimestamp == null) {
            // 获取最新的count条视频
            // rangeWithScores方法参数：start=0, end=count-1 表示取前count条
            // 因为是按分数倒序，所以0是最新的
            videoIdStrs = redisTemplate.opsForZSet().reverseRange(inboxKey, 0, count - 1);
        } else {
            // 分页查询，获取比lastTimestamp更早的视频
            videoIdStrs = redisTemplate.opsForZSet()
                    .reverseRangeByScore(inboxKey, 0, lastTimestamp - 1, 0, count);
        }
        if (videoIdStrs == null || videoIdStrs.isEmpty()) return List.of();
        // 转换为Long类型的视频ID列表
        return  videoIdStrs.stream()
                .map(Integer::parseInt)
                .toList();
    }
    public Long getLastTimestamp(List<Integer> videoIds, String inboxKey) {
        if (videoIds.isEmpty()) {
            return null;
        }
        Integer lastVideoIdStr = new ArrayList<>(videoIds).get(videoIds.size() - 1);
        Double score = redisTemplate.opsForZSet().score(inboxKey, lastVideoIdStr.toString());
        return score != null ? score.longValue() : null;
    }
    public List<Card> getFeedVideo(List<Integer> videoIds) {
        Integer userId = CurrentHolder.getCurrentId();
        List<Card> cardList = null;
        if (videoIds.isEmpty()){
            List<Integer> followedIds = userMapper.getFollowingIds(userId);
            if (followedIds == null || followedIds.isEmpty()) return List.of();
            cardList = cardMapper.queryCardByuserId(followedIds);
        }else {
            cardList = cardMapper.queryCardByIds(videoIds);
        }
        if (cardList == null || cardList.isEmpty()) return List.of();
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
        return cardList;
    }
}
