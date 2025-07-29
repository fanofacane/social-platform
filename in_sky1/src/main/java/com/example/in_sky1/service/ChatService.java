package com.example.in_sky1.service;

import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.ws.pojo.Message;

public interface ChatService {
    Result getChatUserList(Integer id,Integer currentUserId);

    void insert(Message msg);

    Result getChatMessageList(Integer id,Integer currentUserId);

    Result clearUnread(Integer id);
}
