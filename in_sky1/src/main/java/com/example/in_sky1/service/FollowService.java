package com.example.in_sky1.service;

import com.example.in_sky1.dto.UserVO;
import com.example.in_sky1.pojo.PageResult;

public interface FollowService {
    boolean checkIsFollowing(Integer followerId,Integer targetUserId);

    String followUser(Integer followerId, Integer targetUserId);



    PageResult<UserVO> getFollowings(Integer page, Integer size);

    PageResult<UserVO> getFollowers(Integer page, Integer size);
}
