package com.example.in_sky1.utils;

import com.example.in_sky1.controller.UploadController;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class OSSutils {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static final String ACCESS_KEY = "Lq8RJN5PzIMTPYDnCUvsS18gr6Nc4E7ZKDQc1ZzH";
    private static final String KEY = "g1YgqrYXpRWKWxCt_LFI5X0R1Bx1f2DTWiM5gf6K";

    public static String uploadFile(MultipartFile file, String fileName) throws IOException {
        InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[600]; //buff用于存放循环读取的临时数据
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        Auth auth = Auth.create(ACCESS_KEY, KEY);//创建凭证
        String upToken = auth.uploadToken("skyaada"); //上传凭证
        Configuration cfg = new Configuration(Region.autoRegion());
        byte[] uploadBytes  = swapStream.toByteArray();
        UploadManager uploadManager = new UploadManager(cfg);
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        //生成一个新的不重复的文件名
        String newFileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        String objectName = dir + "/" + newFileName;
        try {
            Response response = uploadManager.put(uploadBytes, newFileName, upToken);
            logger.info(response.bodyString());
            return newFileName;
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return null;
    }
}
