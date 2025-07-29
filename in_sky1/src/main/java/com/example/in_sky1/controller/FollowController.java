package com.example.in_sky1.controller;

import com.example.in_sky1.dto.UserVO;
import com.example.in_sky1.pojo.PageResult;
import com.example.in_sky1.pojo.Post;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.service.FollowService;
import com.example.in_sky1.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FollowController.java
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FollowController {
    @Autowired
    private final FollowService followService;

    @Autowired
    private PostService postService;

    // 根据多个用户ID查询动态
    @GetMapping("/dynamics")
    public Result getPostsByUsers(){
        return Result.success(postService.getPostsByFollowed());
    }

    @PostMapping("/{followerId}/followings/{targetUserId}")
    public Result follow(@PathVariable Integer followerId,@PathVariable Integer targetUserId) {
        return Result.success(followService.followUser(followerId,targetUserId));
    }


    @GetMapping("/followings")
    public Result getFollowings(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        PageResult<UserVO> result = followService.getFollowings(page,size);
        return Result.success(result);
    }

    @GetMapping("/followers")
    public Result getFollowers(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        PageResult<UserVO> result = followService.getFollowers(page, size);
        return Result.success(result);
    }
    @GetMapping("/{followerId}/checkfollowers/{targetUserId}")
    public Result checkFollower(@PathVariable Integer followerId,@PathVariable Integer targetUserId){
        return Result.success(followService.checkIsFollowing(followerId,targetUserId));
    }

}
