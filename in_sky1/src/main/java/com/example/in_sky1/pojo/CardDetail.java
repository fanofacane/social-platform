package com.example.in_sky1.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class CardDetail implements Serializable {
        private static final long serialVersionUID = 1L;
        private  Integer id;
        private  String title;//卡片标题
        private  String pictureUrl;//卡片图片
        private  String content;//卡片内容
        private LocalDateTime createTime;//创建时间
        private LocalDateTime updateTime;//更新时间
        private Integer likesCount;//点赞数
        private Integer authorId;
        private AuthorDetail authorDetail;//作者信息
        private List<Commentt> commentsList;//评论
        private String fileType;//文件类型
        private Integer collectionsCount;
        private Boolean isCollection;

}
