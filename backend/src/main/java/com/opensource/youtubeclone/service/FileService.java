package com.opensource.youtubeclone.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String uploadFile(MultipartFile file) throws IOException;

    String checkFileExtension(String fileName) throws Exception;

}