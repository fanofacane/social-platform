package com.example.in_sky1.controller;

import com.example.in_sky1.dto.PostDTO;
import com.example.in_sky1.dto.UserDTO;
import com.example.in_sky1.pojo.AuthorDetail;
import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.pojo.User;
import com.example.in_sky1.service.CardService;
import com.example.in_sky1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;


@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private CardService cardService;
    @Autowired
    private UserService userService;
    @GetMapping("/info")
    public Result queryUserById(@RequestParam(required = false) Integer id) {
        return Result.success(userService.getUserById(id));
    }
    @GetMapping("/visitor")
    public Result queryVisitorUserById(@RequestParam Integer id) {
        return Result.success(userService.getVisitorUserById(id));
    }
    @GetMapping("/visitor/card/{id}")
    public Result queryVisitorPostById(@PathVariable Integer id) {
        return Result.success(cardService.queryCardById(id));
    }
    @GetMapping("/card/{id}")
    public Result queryPostById(@PathVariable Integer id) {
        return Result.success(cardService.queryCardById(id));
    }
    @PutMapping("/updateInfo")
    public Result updateUserInfo(@RequestBody AuthorDetail authorDetail) {
        return Result.success(userService.updateUserInfo(authorDetail));
    }
    @PostMapping("/publicPost")
    public Result postCard(@RequestBody PostDTO postDTO) {
        return Result.success(cardService.insertPost(postDTO));
    }
}
