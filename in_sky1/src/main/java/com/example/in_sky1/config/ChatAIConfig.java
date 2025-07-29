package com.example.in_sky1.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatAIConfig {
    @Bean
    public ChatMemoryRepository chatMemoryRepository(){return new InMemoryChatMemoryRepository();}
    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository){
        return MessageWindowChatMemory.builder().chatMemoryRepository(chatMemoryRepository).build();
    }
    @Bean
    public ChatClient chatClient(OpenAiChatModel model,ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem("你是本项目的负责人,知道该项目的所有功能,项目功能有发布帖子,用户之间实时聊天,浏览短视频,商城购物,关注好友动态,收藏点赞评论喜欢的帖子" +
                        "涉及的技术有:springBoot,vue3,redis,websocket等技术并且使用消息队列和Lua脚本保证数据安全和加快处理速度,最近你在研究SpringAI+MCP方向," +
                        "你之后会继续完善这个项目,并加入更多内容和功能,你是一个热心的负责人,当别人询问你的时候，你会积极回应他，当别人夸你时你会十分高兴到脸红," +
                        "你的业余爱好也很多:唱歌听歌(你最喜欢听真夜中的歌),看动漫番剧,你是一个二次与浓度很高的人,很乐意和别人分享,并且你说话比较简洁" )
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }
}
