package com.example.in_sky1.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commentt implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer postId;
    private User user;
    private Integer authorId;
    private Integer parentCommentId;
    private String content;
    private Integer likesCount;
    private Integer commentsCount;
    private Boolean isLike;
    private LocalDateTime createTime;
    private List<Commentt> replies;
}
