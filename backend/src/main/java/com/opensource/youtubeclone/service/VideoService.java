package com.opensource.youtubeclone.service;

import com.opensource.youtubeclone.model.Video;
import com.opensource.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//@RequiredArgsConstructor
@Service
public class VideoService {

    @Autowired
    private  S3Service s3Service;
    @Autowired
    private  VideoRepository videoRepository;



    public void uploadVideo(MultipartFile multipartFile) throws IOException {
        //upload file to AWS s3
        String videoUrl = s3Service.uploadFile(multipartFile);
        Video video = new Video();
        video.setVideoUrl(videoUrl);
        //Save video to database
        videoRepository.save(video);

    }
}
