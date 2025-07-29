package com.example.in_sky1.service.serviceImpl;

import com.example.in_sky1.dto.ChatUserVO;
import com.example.in_sky1.mapper.ChatMapper;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.pojo.User;
import com.example.in_sky1.service.ChatService;
import com.example.in_sky1.utils.CurrentHolder;
import com.example.in_sky1.ws.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result getChatUserList(Integer id,Integer currentUserId) {
        List<Integer> followingIds = chatMapper.getFollowingIdList(currentUserId);
        List<Integer> chatHistoryIds=chatMapper.getChatHistoryIds(currentUserId);
        chatHistoryIds.add(id);
        chatHistoryIds.add(10);
        //stream流去重
        List<Integer> chatUserIdList= Stream.concat(followingIds.stream(),chatHistoryIds.stream()).distinct().collect(Collectors.toList());

        List<ChatUserVO> chatUserList=chatMapper.getChatUserList(chatUserIdList);

        for (ChatUserVO chatUser : chatUserList) {
            Integer unreadCount=chatMapper.getUnreadCount(chatUser.getId(),currentUserId);
            chatUser.setUnreadCount(unreadCount);
            ChatUserVO message=chatMapper.getLastMessage(chatUser.getId(),currentUserId);
            if(message!=null){
                chatUser.setContent(message.getContent());
                chatUser.setCreateTime(message.getCreateTime());
            }
        }
        return Result.success(chatUserList);
    }

    @Override
    @CacheEvict(cacheNames = "chatMessageCache",key = "{#msg.from<#msg.to?#msg.from:#msg.to,#msg.from<#msg.to?#msg.to:#msg.from}")
    public void insert(Message msg) {
        chatMapper.insert(msg);
    }

    @Override
    @Cacheable(cacheNames = "chatMessageCache",key = "{#userId<#currentUserId?#userId:#currentUserId,#userId<#currentUserId?#currentUserId:#userId}")
    public Result getChatMessageList(Integer userId,Integer currentUserId) {
        List<Message> messageList=chatMapper.getChatList(userId,currentUserId);
        return Result.success(messageList);
    }

    @Override
    public Result clearUnread(Integer id) {
        chatMapper.clearUnread(id,CurrentHolder.getCurrentId());
        return Result.success();
    }
}
