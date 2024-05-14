package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.service.MinioService;
import io.minio.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;

    @Value("${spring.minio.basketName}")
    private String bucketName;

    @Autowired
    public MinioServiceImpl(
            @Value("${spring.minio.url}") String minioUrl,
            @Value("${spring.minio.accessKey}") String accessKey,
            @Value("${spring.minio.secretKey}") String secretKey) {

        minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    @SneakyThrows
    public void safeFile(MultipartFile file, String filename)
    {
        if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())){
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .stream(file.getInputStream(), file.getSize(), -1)
                .build());
    }

    @SneakyThrows
    public InputStream getFileInputStream(String fileName){
        InputStream stream;
        stream = minioClient.getObject(GetObjectArgs
                .builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
        return stream;
    }
}
