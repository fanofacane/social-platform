package com.yupi.mcp.mcpserver.Controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class AIChatController {
    private static final Logger log = LoggerFactory.getLogger(AIChatController.class);
    @Autowired
    private ChatClient chatClient;

    @GetMapping(value = "/ai/answer/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public String generateStreamAsString(@RequestParam String searchText,@RequestParam String userId) {
        return  chatClient.prompt()
                .user(searchText)
                .advisors(a-> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY,userId))
                .call()
                .content();

    }
}
