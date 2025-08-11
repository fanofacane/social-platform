package com.example.in_sky1.filter;

import com.example.in_sky1.utils.CurrentHolder;
import com.example.in_sky1.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1. 获取到请求路径
        String requestURI = request.getRequestURI(); // /employee/login

        //2. 判断是否是登录请求, 如果路径中包含 /login, 说明是登录操作, 放行
        if (requestURI.contains("/login")||requestURI.contains("/posts")||requestURI.contains("/register")||requestURI.contains("/chat")||requestURI.contains("/visitor")||requestURI.contains("/order-tracking")){
            log.info("通用请求, 放行");
            filterChain.doFilter(request, response);
            return;
        }
        //3. 获取请求头中的token
        String token = request.getHeader("token");

        //4. 判断token是否存在, 如果不存在, 说明用户没有登录, 返回错误信息(响应401状态码)
        if (token == null || token.isEmpty()){
            log.info("令牌为空, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //5. 如果token存在, 校验令牌, 如果校验失败 -> 返回错误信息(响应401状态码)
        try {

            Claims claims = JwtUtils.parseToken(token);
            Integer userId = Integer.valueOf(claims.get("id").toString());
//            String s = redisTemplate.opsForValue().get("jwtCache::" + userId);
//            if (token.equals(s)) {
                CurrentHolder.setCurrentId(userId);
                log.info("当前登录员工ID: {}, 将其存入ThreadLocal", CurrentHolder.getCurrentId());
                //6. 校验通过, 放行
                log.info("令牌合法, 放行");
                filterChain.doFilter(request, response);
//            }else {
//                log.info("账号在其他地方登录");
//                return;
//            }
        } catch (Exception e) {
            log.info("令牌非法, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 7.删除ThreadLocal中的数据
        CurrentHolder.remove();
    }
}
