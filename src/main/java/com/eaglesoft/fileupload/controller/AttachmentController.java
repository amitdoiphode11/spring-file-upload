package com.eaglesoft.fileupload.controller;

import com.eaglesoft.fileupload.entity.Attachment;
import com.eaglesoft.fileupload.model.ResponseData;
import com.eaglesoft.fileupload.service.AttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Slf4j
@RequestMapping("/api")
public class AttachmentController {

    private AttachmentService service;

    public AttachmentController(AttachmentService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("uploadFile "+file);
        Attachment attachment = null;
        String downloadUrl = "";
        attachment = service.saveAttachment(file);
        downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(attachment.getId())
                .toUriString();

        return new ResponseData(
                attachment.getFileName(),
                downloadUrl,
                file.getName(),
                file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment;
        attachment = service.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName()
                        + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }
}
