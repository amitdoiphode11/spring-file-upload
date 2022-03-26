package com.eaglesoft.fileupload.service;

import com.eaglesoft.fileupload.entity.Attachment;
import com.eaglesoft.fileupload.repository.AttachmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private AttachmentRepository repository;

    public AttachmentServiceImpl(AttachmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new Exception("File name contains invalid path " + fileName);
            }
            return repository.save(new Attachment(fileName, file.getContentType(), file.getBytes()));

        } catch (Exception e) {
            throw new Exception("Could not save file " + fileName);
        }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return repository
                .findById(fileId)
                .orElseThrow(()->new Exception("file not found with id "+fileId));
    }
}
