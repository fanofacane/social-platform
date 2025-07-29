package com.example.in_sky1.controller;
import com.example.in_sky1.pojo.Commentt;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public Result createComment(@RequestBody Commentt comment) {
        return Result.success(commentService.createComment(comment));
    }

}
