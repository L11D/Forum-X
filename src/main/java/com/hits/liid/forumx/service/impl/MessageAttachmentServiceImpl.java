package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.MessageAttachmentEntity;
import com.hits.liid.forumx.entity.MessageEntity;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.repository.MessageAttachmentRepository;
import com.hits.liid.forumx.service.MessageAttachmentService;
import com.hits.liid.forumx.service.MinioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageAttachmentServiceImpl implements MessageAttachmentService {
    private final MinioService minioService;
    private final MessageAttachmentRepository attachmentRepository;
//    private final FileMetadataRepository repository;

    @SneakyThrows
    public void PrintInfo(MultipartFile file){
        log.info("Название файла: {}", file.getOriginalFilename());
        log.info("Размер файла: {}", file.getSize());
        log.info("Контент файла: {}", new String(file.getBytes()));

    }

    @Transactional
    public UUID safeFile(MultipartFile file){
        MessageAttachmentEntity messageAttachment = MessageAttachmentEntity.of(
                null,
                file.getOriginalFilename(),
                file.getSize(),
                LocalDateTime.now(),
                null
        );
        attachmentRepository.save(messageAttachment);
        minioService.safeFile(file, messageAttachment.getId().toString());
        return messageAttachment.getId();
    }

//    @Transactional
//    public UUID safeFile(MultipartFile file){
//        FileMetadataEntity entity = FileMetadataEntity.of(null, file.getOriginalFilename(), file.getSize(), new Date());
//        FileMetadataEntity savedEntity = repository.save(entity);
//        minioService.safeFile(file, savedEntity.getId().toString());
//        return savedEntity.getId();
//    }
//    @Transactional
//    public Optional<FileMetadataEntity> getFileT(UUID id){
//        return repository.findById(id);
//    }

    @Transactional
    @SneakyThrows
    public String getFileName(UUID id){
        MessageAttachmentEntity messageAttachment = attachmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Attachment not fount"));
        return messageAttachment.getFileName();
    }

    @Transactional
    @SneakyThrows
    public InputStreamResource getFile(UUID id){
        MessageAttachmentEntity messageAttachment = attachmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Attachment not fount"));
        return new InputStreamResource(minioService.getFileInputStream(messageAttachment.getId().toString()));
    }
}
