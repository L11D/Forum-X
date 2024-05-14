package com.hits.liid.forumx.controller;

import com.hits.liid.forumx.service.MessageAttachmentService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/message/attachment")
public class MessageAttachmentController {
    private final MessageAttachmentService attachmentService;

    @PostMapping("upload")
    public UUID upload(@RequestParam("file") MultipartFile file){
        return attachmentService.safeFile(file);
    }
    @GetMapping("download/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable("id") UUID id){

        InputStreamResource streamResource = attachmentService.getFile(id);
        String fileName = attachmentService.getFileName(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        return ResponseEntity.ok()
                .headers(headers)
                .body(streamResource);
    }

}
