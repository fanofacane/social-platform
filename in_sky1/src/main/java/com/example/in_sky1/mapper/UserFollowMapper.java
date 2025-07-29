package com.example.in_sky1.mapper;

import com.example.in_sky1.pojo.UserFollow;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserFollowMapper {
    // 插入关注关系
    @Insert("INSERT INTO user_follow (follower_id, following_id, created_time)" +
            "VALUES (#{followerId}, #{followingId}, #{createdTime})")
    int insert(UserFollow userFollow);

    // 删除关注关系
    @Delete("DELETE FROM user_follow " +
            "WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    int delete(Integer followerId,Integer followingId);

    // 查询是否已关注
    @Select("SELECT COUNT(1) FROM user_follow " +
            "WHERE follower_id = #{followerId} AND following_id = #{followingId}")
    boolean exists(Integer followerId, Integer followingId);


    // 新方法（PageHelper 自动分页）
    List<UserFollow> selectFollowingsByUserId(@Param("followerId") Integer followerId);

    // 同理修改粉丝列表查询
    List<UserFollow> selectFollowersByUserId(@Param("followingId") Integer followingId);

    @Select("SELECT COUNT(*) FROM user_follow " +
            "WHERE follower_id = #{userId} AND following_id = #{targetUserId}")
    boolean existsRelationship(Integer userId, Integer targetUserId);
}
