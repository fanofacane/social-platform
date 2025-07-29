package com.example.in_sky1.service.serviceImpl;

import com.example.in_sky1.dto.UserVO;
import com.example.in_sky1.exception.BusinessException;
import com.example.in_sky1.mapper.UserFollowMapper;
import com.example.in_sky1.mapper.UserMapper;
import com.example.in_sky1.pojo.PageResult;
import com.example.in_sky1.pojo.User;
import com.example.in_sky1.pojo.UserFollow;
import com.example.in_sky1.service.FollowService;
import com.example.in_sky1.utils.CurrentHolder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserFollowMapper userFollowMapper;

    @Override
    public boolean checkIsFollowing(Integer followerId ,Integer targetUserId) {
        return userFollowMapper.exists(followerId, targetUserId);
    }

    @Override
    public String followUser(Integer followerId,Integer targetUserId){
        // 1. 校验用户是否存在
        User follower = userMapper.selectById(followerId);
        User targetUser = userMapper.selectById(targetUserId);
        if (follower == null || targetUser == null) {
            throw new BusinessException("用户不存在");
        }
        // 2. 禁止自己关注自己
        if (followerId.equals(targetUserId)) {
            throw new BusinessException("不能关注自己");
        }
        boolean isFollowing = checkIsFollowing(followerId, targetUserId);
        if (isFollowing) {
            // 4. 删除关注关系
            userFollowMapper.delete(followerId, targetUserId);
            // 5. 更新用户计数器
            userMapper.updateFollowerCount(targetUserId,-1);  // 粉丝数-1
            userMapper.updateFollowingCount(followerId,-1);  // 关注数-1
            return "取关成功";
        }else{
            //4. 保存关注关系
            UserFollow follow = new UserFollow();
            follow.setFollowerId(followerId);
            follow.setFollowingId(targetUserId);
            follow.setCreatedTime(LocalDateTime.now());
            userFollowMapper.insert(follow);
            // 5. 更新用户计数器
            userMapper.updateFollowerCount(targetUserId,1);  // 粉丝数+1
            userMapper.updateFollowingCount(followerId,1);   // 关注数+1
            return "关注成功";
        }
    }


    @Override
    public PageResult<UserVO> getFollowings(Integer page, Integer size) {
        // 1. 启动分页（必须在查询方法前调用）
        PageHelper.startPage(page, size);
        Integer userId=CurrentHolder.getCurrentId();
        // 2. 执行查询（不再需要手动计算 offset）
        List<UserFollow> follows = userFollowMapper.selectFollowingsByUserId(userId);

        // 3. 获取分页信息
        PageInfo<UserFollow> pageInfo = new PageInfo<>(follows);

        // 4. 转换为 VO
        List<UserVO> userList = follows.stream()
                .map(f -> convertToUserVO(userMapper.selectById(f.getFollowingId())))
                .collect(Collectors.toList());

        // 5. 返回分页结果
        return new PageResult<>(pageInfo.getTotal(),userList);
    }


    @Override
    public PageResult<UserVO> getFollowers(Integer page, Integer size) {
        Integer userId=CurrentHolder.getCurrentId();
        PageHelper.startPage(page, size);
        List<UserFollow> followers = userFollowMapper.selectFollowersByUserId(userId);
        PageInfo<UserFollow> pageInfo = new PageInfo<>(followers);

        List<UserVO> userList = followers.stream()
                .map(f -> convertToUserVO(userMapper.selectById(f.getFollowerId())))
                .collect(Collectors.toList());

        return new PageResult<>(pageInfo.getTotal(),userList);
    }
    private UserVO convertToUserVO(User user) {
        if (user == null) {
            return null;  // 或返回一个默认的空对象（根据业务需求）
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setPicture(user.getPicture());
        return userVO;
    }
}
