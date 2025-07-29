package com.example.in_sky1.controller;

import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.pojo.User;
import com.example.in_sky1.service.ChatService;
import com.example.in_sky1.utils.CurrentHolder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/list")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private final ChatClient chatClient;
    @Autowired
    private final ChatMemory chatMemory;
    //查询聊天用户
    @GetMapping
    public Result getChatUserList(@RequestParam(required = false) Integer id){
        return chatService.getChatUserList(id, CurrentHolder.getCurrentId());
    }
    //查询聊天记录
    @GetMapping("/talk")
    public Result getChatMessageList(@RequestParam Integer id){
        return chatService.getChatMessageList(id,CurrentHolder.getCurrentId());
    }
    //清除未读消息
    @PostMapping("/clearUnread/{id}")
    public Result clearUnread(@PathVariable Integer id){
        return chatService.clearUnread(id);
    }
    @GetMapping(value = "/chat",produces = "text/html;charset=utf-8")
    public String chatAI(@RequestParam String msg,@RequestParam String userId){
        return chatClient.prompt()
                .user(msg)
                .advisors(a-> a.param(chatMemory.CONVERSATION_ID,userId))
                .call()
                .content();
    }

}
