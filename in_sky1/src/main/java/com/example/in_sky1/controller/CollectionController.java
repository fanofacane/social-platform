package com.example.in_sky1.controller;

import com.example.in_sky1.dto.CollectionDTO;
import com.example.in_sky1.pojo.Card;
import com.example.in_sky1.pojo.Like;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.service.CollectionService;
import com.example.in_sky1.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private CollectionService collectionService;

    @PostMapping("/toggle")
    public Result toggleCollection(@RequestBody CollectionDTO collectionDTO){
        collectionService.toggleCollection(collectionDTO);
        return Result.success();
    }
    @GetMapping("/getUserCollection")
    public Result getUserCollection(){
        return Result.success(collectionService.getCollectionListByUserId());
    }
}
