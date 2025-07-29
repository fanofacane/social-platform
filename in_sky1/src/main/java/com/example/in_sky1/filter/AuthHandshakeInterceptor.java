package com.example.in_sky1.filter;

import com.example.in_sky1.utils.CurrentHolder;
import com.example.in_sky1.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
@Slf4j
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 从请求参数中获取Token（例如 ws://localhost:8080/ws?token=xxx）
        String token = getTokenFromRequest(request);

        // 验证Token是否有效
        if (token == null || !validateToken(token)) {
            // 返回 HTTP 403 错误，阻止连接
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false; // 中断握手
        }
        Claims claims = JwtUtils.parseToken(token);
        Integer userId = Integer.valueOf(claims.get("id").toString());
        // 将Token或其他信息存入attributes，供后续WebSocket会话使用
        attributes.put("token", token);
        attributes.put("userId", userId);
        return true; // 允许继续握手
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 握手成功后调用，可留空
    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        // 从URL查询参数中获取Token
        UriComponents uri = UriComponentsBuilder.fromUri(request.getURI()).build();
        return uri.getQueryParams().getFirst("token");
        // 如果Token在Header中，使用：request.getHeaders().getFirst("Authorization");
    }

    private boolean validateToken(String token) {
        // 调用Token验证逻辑（JWT解析）
        // 返回true表示有效，false无效
        try {
            Claims claims = JwtUtils.parseToken(token);
            return true;
        } catch (Exception e) {
            log.info("令牌非法, 响应401");
            return false;
        }
    }
}
