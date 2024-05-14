package com.hits.liid.forumx.service;

import com.hits.liid.forumx.entity.MessageEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MessageAttachmentService {
    UUID safeFile(MultipartFile file);
    String getFileName(UUID id);
    InputStreamResource getFile(UUID id);
}
