package com.example.in_sky1.controller;

import com.example.in_sky1.dto.UserDTO;
import com.example.in_sky1.service.UserService;
import com.example.in_sky1.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录Controller
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        log.info("登录: {}", userDTO);
        return userService.login(userDTO);
    }
    @PostMapping("/register")
    public Result registerCard(@RequestBody UserDTO userDTO) {
        return userService.insertUser(userDTO);
    }
    @PostMapping("/logout/{userId}")
    public Result logout(@PathVariable Integer userId) {
        return userService.logout(userId);
    }
}
