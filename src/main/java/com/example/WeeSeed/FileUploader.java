package com.example.WeeSeed;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
//라즈베리파이에 파일(이미지,동영상,음성)을 업로드하는 클래스

public class FileUploader {
    @Value("${upload.directory}")
    private String uploadDirectory;
    @Value("${raspberry.pi.url}")
    private String raspberryPiUrl;
    private final SFTPService sftpService;
    public FileUploader (SFTPService sftpService){
        this.sftpService = sftpService;
    }
    public String uploadImage(MultipartFile image) throws Exception {
        String imageFormat = "";
        String imageFileName = image.getOriginalFilename();
        int imageI = imageFileName.lastIndexOf('.');
        if (imageI > 0) {
            imageFormat = imageFileName.substring(imageI + 1);
        }
        FileName imageName = new FileName(image.getOriginalFilename());
        String imageUrl = imageName.getFileName()+"."+imageFormat;
        byte[] bytes = image.getBytes();
        String remoteFilePath = uploadDirectory +imageUrl;
        sftpService.uploadFile(bytes, remoteFilePath);
        return imageUrl;
    }
    public String uploadVoice(MultipartFile audio) throws Exception {
        String voiceFormat = "";
        String voiceFileName = audio.getOriginalFilename();
        int voiceI = voiceFileName.lastIndexOf('.');
        if (voiceI > 0) {
            voiceFormat = voiceFileName.substring(voiceI + 1);
        }
        FileName audioName = new FileName(audio.getOriginalFilename());
        String voiceUrl = audioName.getFileName()+"."+voiceFormat;
        byte[] audioBytesBytes = audio.getBytes();
        String audioRemoteFilePath = uploadDirectory+voiceUrl;
        sftpService.uploadFile(audioBytesBytes, audioRemoteFilePath);
        return  voiceUrl;
    }
}
