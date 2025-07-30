package com.example.in_sky1.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {
    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucket;
}
