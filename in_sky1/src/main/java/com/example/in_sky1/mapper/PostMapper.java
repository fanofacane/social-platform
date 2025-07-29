package com.example.in_sky1.mapper;

import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PostMapper {
    List<Post> getPostsByFollowed(List<Integer> userIds);

    @Update("update posts set likes_count = IFNULL(likes_count, 0) + #{count} where id=#{postId}")
    void updatePostLikes(Integer postId, int count);

    @Update("update posts set comments_count = comments_count + 1 where id=#{id}")
    void incrementCommentCount(Integer id);

    @Update("update posts set collections_count = collections_count + #{count} where id=#{postId}")
    void updatePostCollection(Integer postId, int count);

}
