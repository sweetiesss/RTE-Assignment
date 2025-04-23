package com.example.apisample.product.service;

public interface S3Service {
    void putObject(String bucketName, String key, byte[] file);
}
