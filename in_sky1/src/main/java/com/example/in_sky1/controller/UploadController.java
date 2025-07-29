package com.example.in_sky1.controller;

import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.utils.AliyunOSSOperator;
import com.example.in_sky1.utils.OSSutils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传: {}", file.getOriginalFilename());
        //将文件交给OSS存储管理
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
//        String url = OSSutils.uploadFile(file, file.getOriginalFilename());
//        log.info("文件上传OSS, url: {}", url);
        return Result.success(url);
    }

}
