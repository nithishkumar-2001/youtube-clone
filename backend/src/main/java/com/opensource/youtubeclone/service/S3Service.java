package com.opensource.youtubeclone.service;

import java.util.UUID;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/*
import com.google.auth.oauth2.GoogleCredentials;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
*/

@Service
//@RequiredArgsConstructor
public class S3Service implements FileService{

    private final String BUCKET_NAME = "youtubeclone-videos";

/*    @Value("${spring.cloud.gcp.credentials.location}")
    private String gcpConfigFileName;

    @Value("${spring.cloud.gcp.project-id}")
    private String gcpProjectId;*/

    @Autowired
    private  Storage storage;

    @Override
    public String uploadFile(MultipartFile file) {
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fullFileName = null;
        try {
            fullFileName = UUID.randomUUID().toString() + "." + checkFileExtension(file.getOriginalFilename());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File name is invalid");
        }
        BlobId blobId = BlobId.of(BUCKET_NAME, fullFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType()).build();
        //Authenticated url with time limitation
//        URL url = storage.signUrl(blobInfo, 15, TimeUnit.HOURS, Storage.SignUrlOption.withV4Signature());
        Blob blob;

        //Upload the file to GCP Buckets
        try{
            blob = storage.create(blobInfo, file.getBytes());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An exception occurred while uploading a file");
        }
        return blob.getMediaLink();
    }

 /*   @Override
    public String uploadFile(MultipartFile file) {
        //Upload the file to GCP Buckets
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fullFileName = UUID.randomUUID().toString() + checkFileExtension(fileExtension);
        Path path = new File(file.getOriginalFilename()).toPath();
        try {
            String contentType = Files.probeContentType(path);
            byte[] fileData = FileUtils.readFileToByteArray(convertFile(file));
            InputStream inputStream = new ClassPathResource(StringUtils.replace(gcpConfigFileName,"classpath:","")).getInputStream();
            StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();
            Storage storage = options.getService();
            Bucket bucket = storage.get(BUCKET_NAME,Storage.BucketGetOption.fields());
            Blob blob = bucket.create(fullFileName,fileData,contentType);
            if(blob)



        } catch (Exception e) {

        }

        BlobId blobId = BlobId.of(BUCKET_NAME, fullFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType()).build();
        URL url = storage.signUrl(blobInfo, 15, TimeUnit.HOURS, Storage.SignUrlOption.withV4Signature());
        Blob blob;
        try{
            blob = storage.create(blobInfo, file.getBytes());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An exception occurred while uploading a file");
        }
        return url.toString();
    }
*/
/*
    public File convertFile(MultipartFile file){
        try {
            if(file.getOriginalFilename() == null) {
                throw new Exception("Original file name is null");
            }
            File convertedFile = new File(file.getOriginalFilename());
            FileOutputStream outputStream = new FileOutputStream(convertedFile);
            outputStream.write(file.getBytes());
            outputStream.close();
            return convertedFile;
        } catch (Exception e) {
            throw new Exception("An Error occurred while converting the file");
        }
    }
*/

    public String checkFileExtension(String fileName) throws Exception {
        if(fileName != null && fileName.contains(".")) {
            String[] extensions = {
                    ".MOV", ".MPEG-1", ".MPEG-2", ".MPEG4", ".MP4", ".MPG", ".AVI", ".WMV",
                    ".MPEGPS", ".FLV", ".3GPP", ".WebM", ".DNxHR", ".ProRes", ".CineForm", ".HEVC"
            };
            for (String ext: extensions) {
                if(fileName.endsWith(ext) || fileName.endsWith(ext.toLowerCase())) {
                    return ext;
                }
            }
        }
        throw new Exception("Not permitted file type");
    }
}
