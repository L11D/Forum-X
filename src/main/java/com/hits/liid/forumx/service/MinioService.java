package com.hits.liid.forumx.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioService {
    void safeFile(MultipartFile file, String filename);
    InputStream getFileInputStream(String fileName);
}
