package com.yupi.mcp.mcpserver;

import com.yupi.mcp.mcpserver.service.MianshiyaService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@ServletComponentScan
@SpringBootApplication
@EnableCaching
public class McpServerApplication {
    public static void main(String[] args){
        SpringApplication.run(McpServerApplication.class, args);
    }
    @Bean
    public ChatMemory chatMemory() { return  new InMemoryChatMemory();}
    @Bean
    public ToolCallbackProvider serverTools(MianshiyaService mianshiyaService) {
        return MethodToolCallbackProvider.builder().toolObjects(mianshiyaService).build();
    }
    @Bean
    public ChatClient initChatClient(ChatClient.Builder chatClientBuilder,
                                     ToolCallbackProvider serverTools,ChatMemory chatMemory) {
        ChatClient chatClient = chatClientBuilder
                .defaultSystem("你是智能客服,会根据用户想要询问的信息给出相应的回答,特别是当询问想要查询作品信息时，你会严格的返回" +
                        "链接加标题加媒体源的格式给用户，可以参考以下格式：\"<a href ='\"+link+\"' style='margin-right: 95px;text-decoration: none;'>\"+title+\"</a>\"+\"<video src='\"+mediaUrl+\"' controls style='width:150px;height:150px;object-fit:cover;'></video>\"" )
                .defaultTools(serverTools)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
        return chatClient;
    }

}
