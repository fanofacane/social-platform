package com.example.in_sky1.service.serviceImpl;

import com.example.in_sky1.mapper.CollectionsMapper;
import com.example.in_sky1.mapper.LikeMapper;
import com.example.in_sky1.mapper.PostMapper;
import com.example.in_sky1.mapper.UserMapper;
import com.example.in_sky1.pojo.Post;
import com.example.in_sky1.pojo.State;
import com.example.in_sky1.pojo.User;
import com.example.in_sky1.service.PostService;
import com.example.in_sky1.utils.CurrentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private CollectionsMapper collectionsMapper;
    @Override
    public List<Post> getPostsByFollowed() {
        Integer currentUserId= CurrentHolder.getCurrentId();
        List<Integer> followedIds=userMapper.getFollowingIds(currentUserId);
        if (followedIds == null || followedIds.isEmpty()) {
            return null;
        }
        List<Post> posts=postMapper.getPostsByFollowed(followedIds);
        for (Post post : posts) {
            Integer commentId=null;
            boolean likedExists = likeMapper.checkLikeExists(currentUserId, post.getId(), commentId);
            boolean collectionExists = collectionsMapper.checkCollectionExists(currentUserId, post.getId());
            State state=new State();
            state.setIsFollow(true);
            state.setIsCollection(collectionExists);
            state.setIsLike(likedExists);
            User user=userMapper.selectUserById(post.getAuthorId());
            post.setUser(user);
            post.setState(state);
        }
        return posts;
    }
}
