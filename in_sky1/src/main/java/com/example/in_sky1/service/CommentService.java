package com.example.in_sky1.service;

import com.example.in_sky1.pojo.Commentt;

import java.util.List;

public interface CommentService {
    List<Commentt> getCommentTree(Integer postId,Integer userId);

    Commentt createComment(Commentt comment);
}
