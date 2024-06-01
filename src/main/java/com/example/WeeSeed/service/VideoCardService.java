package com.example.WeeSeed.service;

import com.example.WeeSeed.SFTPService;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoCardService {

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @Value("${upload.directory}")
    private String uploadDirectory;
    @Value("${raspberry.pi.url}")
    private String raspberryPiUrl;
    private final SFTPService sftpService;

//    private String saveFile(MultipartFile file) throws IOException {
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        SslProperties.Bundles.Watch.File videoFile = new SslProperties.Bundles.Watch.File("uploads/" + fileName);
//        videoFile.getParentFile().mkdirs(); // 디렉토리가 존재하지 않으면 생성
//        file.transferTo(videoFile);
//        return videoFile.getAbsolutePath();
//    }
    private String createThumbnail(String videoPath) throws IOException, InterruptedException {
        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);

        String thumbnailPath = "uploads/" + FilenameUtils.getBaseName(videoPath) + ".png";
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoPath)
                .addOutput(thumbnailPath)
                .setFrames(1)
                .setVideoFilter("select='gte(n\\,10)'")  // 동영상의 10번째 프레임을 선택
                .done();

        executor.createJob(builder).run();

        return thumbnailPath;
    }
    public void saveVideoCard(String cardName, MultipartFile video,String color,String childCode,String constructorId){
        //String thumbnailPath = createThumbnail(videoPath);
    }
}
