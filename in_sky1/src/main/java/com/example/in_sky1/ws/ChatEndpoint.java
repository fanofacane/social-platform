package com.example.in_sky1.ws;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.example.in_sky1.service.ChatService;
import com.example.in_sky1.utils.CurrentHolder;
import com.example.in_sky1.utils.JwtUtils;
import com.example.in_sky1.ws.pojo.Message;
import com.example.in_sky1.ws.pojo.MessageVo;
import com.qiniu.util.Json;
import io.jsonwebtoken.Claims;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Integer.valueOf;

@Slf4j
@ServerEndpoint(value = "/chat")
@Component
public class ChatEndpoint {
    private static final Map<String, Session> onlineUser = new ConcurrentHashMap<>();
    private static ApplicationContext context;
    private static ChatService chatService; // 静态变量（可选）

    @Autowired
    public void setChatService(ChatService chatService) {
        ChatEndpoint.chatService = chatService; // 通过Setter注入到静态变量
    }

    @OnOpen
    public void onOpen(Session session) throws IOException{
        // 从Session中获取请求参数
        String token = session.getRequestParameterMap().get("token").get(0);
        if(token == null||validateToken(token)==0) {
            // 验证失败，关闭连接
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Token无效"));
            return;
        }
        onlineUser.put(validateToken(token).toString(), session);
        log.info("用户={}建立连接,当前在线人数={}",  validateToken(token).toString(),onlineUser.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("服务器端收到消息:{}", message);
        try {
            Message msg = JSON.parseObject(message, Message.class);
            msg.setCreateTime(LocalDateTime.now());
            chatService.insert(msg);
            Session session = onlineUser.get(msg.getTo());
            if(session!=null) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            log.error("发送消息失败");
        }
    }

    @OnClose
    public void onClose(Session session) {
        String token = session.getRequestParameterMap().get("token").get(0);
        String userId = validateToken(token).toString();
        onlineUser.remove(userId);
    }
    public void sendAllMessage(String message) {
        try {
            for (Session session : onlineUser.values()) {
                log.info("服务器端给客户端={}发送消息={}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.info("服务端发送消息给客户端失败", e);
        }
    }
    public void sendOfflineNotice(String userId){
        Session session = onlineUser.get(userId);
        if(session!=null) {
            try {
                MessageVo messageVo=new MessageVo();
                messageVo.setTo(userId);
                messageVo.setOrder("logout");
                messageVo.setContent("你已经在其他地方登录");
                messageVo.setTime(LocalDateTime.now());
                String jsonStr = JSONUtil.toJsonStr(messageVo);
                session.getBasicRemote().sendText(jsonStr);
            } catch (IOException e) {
                log.info("服务端发送消息给客户端失败", e);
            }
        }
    }
    private Integer validateToken(String token) {
        // 调用Token验证逻辑（JWT解析）
        // 返回true表示有效，false无效
        try {
            Claims claims = JwtUtils.parseToken(token);
            Integer userId = Integer.valueOf(claims.get("id").toString());
            return userId;
        } catch (Exception e) {
            log.info("令牌非法, 响应401");
            return 0;
        }
    }
}
