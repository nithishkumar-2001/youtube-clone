package com.opensource.youtubeclone.controller;

import com.opensource.youtubeclone.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/videos")
//@RequiredArgsConstructor
public class VideoController {

    @Autowired
    private VideoService videoService;
    Logger logger = LoggerFactory.getLogger(VideoController.class);


    @PostMapping("/uploadVideo")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadVideo(@RequestParam("file") MultipartFile file) {

       try {
           videoService.uploadVideo(file);
       } catch (Exception e) {
           logger.error(e.getMessage());
       }

    }


}
