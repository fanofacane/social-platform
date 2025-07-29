package com.example.in_sky1.mapper;

import com.example.in_sky1.dto.UserDTO;
import com.example.in_sky1.dto.UserVO;
import com.example.in_sky1.pojo.AuthorDetail;
import com.example.in_sky1.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from users where id=#{id}")
    AuthorDetail getUserById(Integer id);

    @Select("select * from users where id=#{id}")
    User selectUserById(Integer id);
    /**
     * 根据用户名和密码查询员工信息
     */
    @Select("select id, username ,picture from users where username = #{username} and password = #{password}")
    UserVO selectByUsernameAndPassword(UserDTO userDTO);

    @Select("select * from users where id=#{id}")
    User selectById(Integer id);

    @Update("UPDATE users " +
            "SET follower_count = follower_count + #{increment} " +
            "WHERE id=#{id}")
    void updateFollowerCount(Integer id,@Param("increment") Integer i);
    @Update("UPDATE users " +
            "SET following_count = following_count + #{increment} " +
            "WHERE id=#{id}")
    void updateFollowingCount(Integer id,@Param("increment") Integer i);

    @Select("select following_id from user_follow where follower_id=#{id}")
    List<Integer> getFollowingIds(Integer id);

    @Select("SELECT COALESCE(SUM(p.likes_count), 0) AS total_likes FROM users u LEFT JOIN posts p ON u.id = p.author_id WHERE u.id = #{userId}")
    Integer totalLike(Integer userId);

    @Update("update users set username=#{username},bio=#{bio},picture=#{picture} where id=#{id}")
    void updateUserInfo(AuthorDetail authorDetail);

    @Select("select COUNT(1) from users where username=#{username}")
    boolean existUsername(String username);

    @Insert("INSERT INTO users (username,password,age,bio,ip,picture) values (#{username},#{password},#{age},#{bio},#{ip},#{picture})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(UserDTO userDTO);
}
