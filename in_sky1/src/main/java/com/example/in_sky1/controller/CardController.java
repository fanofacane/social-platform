package com.example.in_sky1.controller;

import com.alibaba.fastjson.JSON;
import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.CardDetail;
import com.example.in_sky1.pojo.Commentt;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.service.CardService;
import com.example.in_sky1.service.CommentService;
import com.example.in_sky1.utils.CurrentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RequestMapping("/posts")
@RestController
public class CardController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CardService cardService;
    //游标查询帖子
    @GetMapping
    public Result getCard(@RequestParam(value = "userId",required = false) Integer userId,
                          @RequestParam(required = false) String fileType,
                          @RequestParam(required = false) Integer cursor,
                          @RequestParam(defaultValue = "5") int size) {
        return Result.success(cardService.queryMoreCard(userId, fileType, cursor, size));
    }

    //查询帖子详情
    @GetMapping("/{id}")
    public Result getCardById(@PathVariable Integer id, @RequestParam(value = "userId",required = false) Integer userId) {
        return Result.success(cardService.getCardById(id,userId));
    }
    //关键字查询
    @GetMapping("/all")
    public Result getCardByKey(@RequestParam String key,@RequestParam(required = false) Integer userId) {
            return Result.success(cardService.getCardByKey(key,userId));
    }
    //获取评论信息
    @GetMapping("/comments/{postId}")
    public Result getPostComments(@PathVariable Integer postId,@RequestParam(required = false) Integer userId) {
        return Result.success(commentService.getCommentTree(postId, userId));
    }
}
