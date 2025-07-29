package com.example.in_sky1.mapper;

import com.example.in_sky1.pojo.Commentt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Commentt> getTopLevelComments(Integer postId);

    List<Commentt> getRepliesByParentId(Integer parentId);

    @Update("update comments set likes_count = IFNULL(likes_count, 0) + #{count} where id = #{commentId}")
    void updateCommentLikes(Integer commentId, int count);

    List<Commentt> selectRepliesByParentId(Integer id);

    void insertComment(Commentt comment);

    @Update("update comments set comments_count = comments_count + 1 where id=#{parentCommentId}")
    Integer updateCommentCount(Integer parentCommentId);
}
