package com.example.in_sky1.utils;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

@Component
@Slf4j
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    /**
     * 上传文件到minio，并返回访问该文件的url
     * @param bucket 桶名称
     * @param multipartFile MultipartFile对象
     * @return
     */
    public String uploadFile(String bucket, MultipartFile multipartFile){
        String localFileAbsolutePath = null;
        String minioPath = null;
        try {
            // TODO 下面用this来调用的都是抽取出来的方法，我这里将它们都放在这个工具类下了。你也可以将这些方法放到其他工具类下
            // 获取文件扩展名
            String extension = getExtension(multipartFile);
            // 获取mimeType
            String mimeType = getMimeType(extension);
            // 获取本地文件的路径（源路径）
            File localFile = File.createTempFile("minio", ".temp");
            multipartFile.transferTo(localFile);
            localFileAbsolutePath = localFile.getAbsolutePath();
            // 构造minio中的路径（目标路径）=年+月+日+md5值+扩展名
            // TODO 下面的folder是上传后的文件在桶里面的路径。你也可以不指定，若不指定文件上传后就直接在桶下
//            String folder = "other/" + DateUtil.generateFolderOfCurrentDate();
            String folder = "sky/";
            // TODO 实际开发中，通常将文件的md5值作为文件名。当然你也可以用其他的作为文件名
            String fileName = DigestUtils.md5Hex(new FileInputStream(localFile));
            minioPath = folder + fileName + extension;
            // 构造UploadObjectArgs对象
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .filename(localFileAbsolutePath)// 本地文件的路径（源路径）
                    .object(minioPath)// minio中的路径（目标路径）
                    .contentType(mimeType)
                    .build();
            // 上传文件
            minioClient.uploadObject(uploadObjectArgs);
        } catch (Exception e) {
            log.error("文件上传失败！本地源文件路径：{}，minio目标文件路径：{}，错误信息：{}", localFileAbsolutePath, minioPath, e.getMessage());
            e.printStackTrace();
            // TODO 这里你可以抛出你自定义的业务异常
            // throw new FileUploadFailedException(MessageConstant.FILE_UPLOAD_FAILED);
        }
        log.info("文件上传成功！本地源文件路径：{}，minio目标文件路径：{}", localFileAbsolutePath, minioPath);
        // 返回访问该文件的url
        return "http://47.96.156.182:9000/" + bucket + "/" + minioPath;
    }

    /**
     * 根据MultipartFile对象获取其文件扩展名
     * @param multipartFile MultipartFile对象
     * @return
     */
    private static String getExtension(MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf("."));
        return extension;
    }

    /**
     * 根据扩展名获取文件类型（mimetype）
     * @param extension 文件的扩展名，如：“.png”
     * @return
     */
    public static String getMimeType(String extension) {
        if (extension == null){
            extension = "";
        }
        // 根据文件扩展名获取文件类型
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        if (extensionMatch != null){
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }
}
