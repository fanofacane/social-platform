package com.example.in_sky1.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.in_sky1.mapper.CommentMapper;
import com.example.in_sky1.mapper.LikeMapper;
import com.example.in_sky1.mapper.PostMapper;
import com.example.in_sky1.mapper.UserMapper;
import com.example.in_sky1.pojo.CacheTTL;
import com.example.in_sky1.pojo.Commentt;
import com.example.in_sky1.pojo.User;
import com.example.in_sky1.service.CommentService;
import com.example.in_sky1.utils.CacheExpireUtils;
import com.example.in_sky1.utils.CurrentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CacheExpireUtils cacheExpireUtils;
    @Override
    public List<Commentt> getCommentTree(Integer postId,Integer userId) {
        List<Commentt> topComments = commentMapper.getTopLevelComments(postId);
        if(!topComments.isEmpty()) {
            topComments.forEach(comment ->
                    comment.setReplies(getNestedReplies(comment.getId()))
            );
            for (Commentt commentt : topComments) {
                User user=userMapper.selectById(commentt.getAuthorId());
                commentt.setUser(user);
                commentt.setIsLike(likeMapper.checkLikeExists(userId,null,commentt.getId()));
            }
            return topComments;
        }else {
            return null;
        }
    }

    @Override
    public Commentt createComment(Commentt comment) {
        Integer currentUserId=CurrentHolder.getCurrentId();
        comment.setAuthorId(currentUserId);
        if (comment.getParentCommentId()!=null){
            commentMapper.updateCommentCount(comment.getParentCommentId());
        }
        // 插入评论，并获取自增ID
        commentMapper.insertComment(comment);
        // 增加帖子评论计数
        postMapper.incrementCommentCount(comment.getPostId());
        // 查询用户信息并设置到评论中
        User user = userMapper.selectById(currentUserId);
        comment.setUser(user);
        comment.setLikesCount(0);
        comment.setCreateTime(LocalDateTime.now());
        // 返回完整的评论数据
        return comment;
    }
    // 递归获取嵌套回复
    private List<Commentt> getNestedReplies(Integer parentId) {
        List<Commentt> replies = commentMapper.getRepliesByParentId(parentId);
        replies.forEach(reply ->{
            User user = userMapper.selectById(reply.getAuthorId());
            reply.setUser(user);
            reply.setReplies(getNestedReplies(reply.getId()));
                });
        return replies;
    }
}
