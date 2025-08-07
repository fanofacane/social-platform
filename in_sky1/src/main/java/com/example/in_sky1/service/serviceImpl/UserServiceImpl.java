package com.example.in_sky1.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.example.in_sky1.dto.UserDTO;
import com.example.in_sky1.dto.UserVO;
import com.example.in_sky1.mapper.ChatMapper;
import com.example.in_sky1.mapper.UserMapper;
import com.example.in_sky1.pojo.AuthorDetail;
import com.example.in_sky1.pojo.CacheTTL;
import com.example.in_sky1.pojo.LoginInfo;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.service.ChatService;
import com.example.in_sky1.service.UserService;
import com.example.in_sky1.utils.CacheExpireUtils;
import com.example.in_sky1.utils.CurrentHolder;
import com.example.in_sky1.utils.JwtUtils;
import com.example.in_sky1.ws.ChatEndpoint;
import com.example.in_sky1.ws.pojo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CacheExpireUtils cacheExpireUtils;
    @Autowired
    private ChatEndpoint chatEndpoint;
    @Autowired
    private ChatMapper chatMapper;
    @Override
    public Result login(UserDTO userDTO) {
        if (userDTO.getUsername()=="" || userDTO.getPassword()=="") return Result.error("用户名或者密码不能为空");
        UserVO user = userMapper.selectByUsernameAndPassword(userDTO);
        if (user==null) return Result.error("用户名和密码错误");
        log.info("登录成功, 员工信息: {}", user);
        //判断是否已经登录
        String s = redisTemplate.opsForValue().get("jwtCache::" + user.getId());
        if(s != null&&!s.isEmpty()){
            //通知下线
            chatEndpoint.sendOfflineNotice(user.getId().toString());
            redisTemplate.delete("jwtCache::" + user.getId());
        }
        //生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String jwt = JwtUtils.generateToken(claims);
        redisTemplate.opsForValue().set("jwtCache::" + user.getId(),jwt);
        LoginInfo loginInfo=new LoginInfo(user.getId(), user.getUsername(),user.getPicture(),jwt);
        return Result.success(loginInfo);
    }

    @Override
    public AuthorDetail getUserById(Integer id) {
        int ttl=cacheExpireUtils.randomExpire(CacheTTL.USER_CACHE_BASE);
        AuthorDetail authorDetail;
        Integer userId = id == null ? CurrentHolder.getCurrentId() : id;
        String s = redisTemplate.opsForValue().get("userInfoCache::" + userId);
        if(s == null||s.isEmpty()){
            Integer totalLike=userMapper.totalLike(userId);
            authorDetail=userMapper.getUserById(userId);
            authorDetail.setLikeCount(totalLike);
            String jsonStr = JSONUtil.toJsonStr(authorDetail);
            redisTemplate.opsForValue().set("userInfoCache::"+userId,jsonStr,ttl, TimeUnit.SECONDS);
            return authorDetail;
        }else {
            return JSONUtil.toBean(s, AuthorDetail.class);
        }
    }

    @Override
    public String updateUserInfo(AuthorDetail authorDetail) {
        try {
            Integer userId=CurrentHolder.getCurrentId();
            authorDetail.setId(userId);
            authorDetail.setPicture(authorDetail.getPicture());
            userMapper.updateUserInfo(authorDetail);
            redisTemplate.delete("userInfoCache::"+userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    @Override
    public Result insertUser(UserDTO userDTO){
        Boolean exist = userMapper.existUsername(userDTO.getUsername());
        if(exist){
            return Result.error("用户名重复");
        }
        if (userDTO.getUsername()=="" || userDTO.getPassword()==""){
            return Result.error("用户名和密码不能为空");
        }
        UserDTO userDTO1=new UserDTO();
        BeanUtil.copyProperties(userDTO,userDTO1);
        userDTO1.setAge(18);
        userDTO1.setBio("还没有设置个性签名qwq");
        userDTO1.setIp("未知");
        userDTO1.setPicture("http://47.96.156.182:9000/origin/sky/3e9ce3813b7199ea9588eeb920f41208_512_512.jpg");
        Message message=new Message();
        userMapper.insert(userDTO1);
        message.setTo(userDTO1.getId().toString());
        message.setFrom("10");
        message.setContent("你好,我是你的AI客服,有什么可以帮助到你的吗");
        message.setCreateTime(LocalDateTime.now());
        chatMapper.insert(message);
        return Result.success("注册成功");
    }

    @Override
    public Result logout(Integer userId) {
        redisTemplate.delete("jwtCache::" + userId);
        return Result.success("退出成功");
    }

    @Override
    public AuthorDetail getVisitorUserById(Integer userId) {
        AuthorDetail authorDetail;
        Integer totalLike=userMapper.totalLike(userId);
        authorDetail=userMapper.getUserById(userId);
        authorDetail.setLikeCount(totalLike);
        return authorDetail;
    }
}
