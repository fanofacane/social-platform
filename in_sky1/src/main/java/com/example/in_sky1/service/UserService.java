package com.example.in_sky1.service;

import com.example.in_sky1.dto.UserDTO;
import com.example.in_sky1.pojo.AuthorDetail;
import com.example.in_sky1.pojo.LoginInfo;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserService {
    Result login(UserDTO userDTO);

    AuthorDetail getUserById(Integer id);

    String updateUserInfo(AuthorDetail authorDetail);

    Result insertUser(UserDTO userDTO);

    Result logout(Integer userId);

    AuthorDetail getVisitorUserById(Integer id);
}
