package com.example.in_sky1.mapper;

import com.example.in_sky1.dto.PostDTO;
import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.CardDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CardMapper {
    @Select("select * from posts")
    public List<Card> queryAllCard();

    @Select("select * from posts where id=#{id}")
    CardDetail getCardById(Integer id);

    List<Card> getCardByKey(String key);

    @Select("select * from posts where author_id=#{id}")
    List<Card> queryCardById(Integer id);

    @Insert("insert into posts(title,content,picture_url,author_id,create_time,update_time,file_type)values (#{title},#{content},#{pictureUrl},#{authorId},#{createTime},#{updateTime},#{fileType})")
    void insert(PostDTO postDTO);

    List<Card> queryMoreCard(int size);

    PostDTO findById(Integer cursor);

    List<Card> findNextPage(Integer cursorId, LocalDateTime cursorTime, int size);
}
