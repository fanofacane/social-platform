package com.example.in_sky1.controller;

import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.Like;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// LikeController.java
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/toggle")
    public Result toggleLike(@RequestBody Like like){
        return Result.success(likeService.toggleLike(like));
    }
    @GetMapping("/status")
    public Result checkLikeStatus(@RequestParam(required = false) Integer postId, @RequestParam(required = false) Integer commentId) {
        return Result.success(likeService.Liked(postId, commentId));
    }
    @GetMapping("/getUserLike")
    public Result getUserLike() {
        return Result.success(likeService.getLikeListByUserId());
    }
}