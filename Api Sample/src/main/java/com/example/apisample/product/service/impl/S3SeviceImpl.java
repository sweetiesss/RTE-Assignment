package com.example.apisample.product.service.impl;

import com.example.apisample.product.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3SeviceImpl implements S3Service {
    private final S3Client s3Client;

    public S3SeviceImpl(S3Client s3Client){
        this.s3Client = s3Client;
    }

    @Override
    @Transactional
    public void putObject(String bucketName, String key, byte[] file){

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(objectRequest, RequestBody.fromBytes(file));
    }

}
