package com.example.in_sky1.mapper;

import com.example.in_sky1.dto.ChatUserVO;
import com.example.in_sky1.pojo.User;
import com.example.in_sky1.ws.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChatMapper {
    List<ChatUserVO> getChatUserList(List<Integer> ids);

    List<Integer> getFollowingIdList(Integer currentId);

    @Insert("insert into chat_messages(`from`,`to`,content,create_time)values (#{from},#{to},#{content},#{createTime})")
    void insert(Message msg);

    @Select("select * from chat_messages where (`from` = #{userId} AND `to` = #{currentId}) or (`from` = #{currentId} AND `to` = #{userId})")
    List<Message> getChatList(Integer userId,Integer currentId);

    @Select("select count(*) from chat_messages where `from`=#{userId} and `to`=#{currentId} and is_read = 0 ")
    Integer getUnreadCount(Integer userId, Integer currentId);
    @Select("SELECT * FROM chat_messages WHERE (`from` = #{userid} AND `to` = #{currentId}) or (`from` = #{currentId} AND `to` = #{userid}) ORDER BY create_time DESC LIMIT 1")
    ChatUserVO getLastMessage(Integer userid, Integer currentId);

    @Update("update chat_messages set is_read = 1 where `from`=#{id} and `to` =#{currentId} and is_read= 0 ")
    void clearUnread(Integer id,Integer currentId);

    List<Integer> getChatHistoryIds(Integer userId);
}
