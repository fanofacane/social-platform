package com.example.in_sky1.mapper;

import com.example.in_sky1.dto.CollectionDTO;
import com.example.in_sky1.pojo.Card;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollectionsMapper {
    @Select("select count(1) from collections where user_id=#{userId} and post_id=#{postId}")
    boolean checkCollectionExists(Integer userId, Integer postId);

    @Delete("DELETE FROM collections WHERE user_id = #{userId} and post_id=#{postId}")
    void deleteCollection(Integer userId, Integer postId);
    @Insert("INSERT INTO collections(user_id,post_id,create_time) VALUES (#{userId},#{postId},#{createTime})")
    void insertCollection(CollectionDTO collectionDTO);

    List<Card> getCollectionListByUserId(Integer userId);
}
