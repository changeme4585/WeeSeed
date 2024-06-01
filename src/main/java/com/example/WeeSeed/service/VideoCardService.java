package com.example.WeeSeed.service;

import com.example.WeeSeed.FileName;
import com.example.WeeSeed.SFTPService;
import com.example.WeeSeed.dto.VideoDto;
import com.example.WeeSeed.entity.videoCard;
import com.example.WeeSeed.repository.VideoCardRepository;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoCardService {
    private final VideoCardRepository videoCardRepository;

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Value("${raspberry.pi.url}")
    private String raspberryPiUrl;
    private final SFTPService sftpService;

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File videoFile = new File("/Users/joseungbin/Desktop/video" + fileName);
        videoFile.getParentFile().mkdirs(); // 디렉토리가 존재하지 않으면 생성
        file.transferTo(videoFile);
        return videoFile.getAbsolutePath();
    }
    private String createThumbnail(String videoPath) throws IOException {

        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);


        String thumbnailPath = "/Users/joseungbin/Desktop/thumbnail" + FilenameUtils.getBaseName(videoPath) + ".png";
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoPath)
                .addOutput(thumbnailPath)
                .setFrames(1)
                .setVideoFilter("select='gte(n\\,10)'")  // 동영상의 10번째 프레임을 선택
                .done();


        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        executor.createJob(builder).run();

        return thumbnailPath;
    }
    public void saveVideoCard(String cardName, MultipartFile video,String color,String childCode,String constructorId,MultipartFile thumbnail){
        //String thumbnailPath = createThumbnail(videoPath);
        String videoFormat = "";
        String videoFileName = video.getOriginalFilename();
        int videoI = videoFileName.lastIndexOf('.');
        if(videoI > 0){
            videoFormat = videoFileName.substring(videoI + 1);
        }
        FileName videoName = new FileName(video.getOriginalFilename());
        String videoUrl  =  videoName.getFileName() +"." +videoFormat;


        String thumbNailFormat = "";
        String thumbNailFileName = thumbnail.getOriginalFilename();
        int thumbNailI = thumbNailFileName.lastIndexOf('.');
        if(thumbNailI > 0){
            thumbNailFormat = thumbNailFileName.substring(thumbNailI + 1);
        }
        FileName thumbNailName = new FileName(thumbnail.getOriginalFilename());
        String thumbNailUrl  =  thumbNailName.getFileName() +"." +thumbNailFormat;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        String formattedDate = now.format(formatter); //현재시간을 String 형으로


//        File file = new File(video.getOriginalFilename());
//        video.transferTo(file);
//        try {
//            String videoPath = saveFile(video);
//            String thumbnailPath = createThumbnail(videoPath);
//        }catch (Exception e) {
//            System.out.println("썸네일 이미지 로직 에러: "+e);
//        }




        videoCard videocard = videoCard.builder().
                cardName(cardName).
                creationTime(formattedDate).
                videoUrl(videoUrl).
                childId(childCode).
                thumbnailUrl(thumbNailUrl).
                userId(constructorId).
                color(color).
                build();
        videoCardRepository.videoCardSave(videocard);

        try{
            byte [] bytes = video.getBytes();
            String remoteFilePath = uploadDirectory +videoUrl;
            sftpService.uploadFile(bytes, remoteFilePath);



            byte [] thumbNailbytes = thumbnail.getBytes();
            String thumbNailRemoteFilePath = uploadDirectory +thumbNailUrl;
            sftpService.uploadFile(thumbNailbytes, thumbNailRemoteFilePath);
        }catch (Exception e){
            System.out.println("라즈베리파이 동영상 업로드 이슈: "+e);
        }
    }

    public List<VideoDto> getVideoCard(String childCode, String constructorId){
            List<videoCard> videoCardList =  videoCardRepository.getVideoCardList(childCode,constructorId);
            List<VideoDto> videoDtoList  = new ArrayList<>();
            for (videoCard videoCard:videoCardList){
                String videoUrl = raspberryPiUrl + videoCard.getVideoUrl();
                String thumbNailUrl = raspberryPiUrl + videoCard.getThumbnailUrl();
                        VideoDto videoDto = VideoDto.builder().
                        videoCardId(videoCard.getVideoCardID()).
                        cardName(videoCard.getCardName()).
                        creationTime(videoCard.getCreationTime()).
                        childId(videoCard.getChildId()).
                        constructorId(videoCard.getUserId()).
                        thumbNail(thumbNailUrl).
                        video(videoUrl).
                        color(videoCard.getColor()).
                        build();
//
                videoDtoList.add(videoDto);
            }
         return videoDtoList;
    }

}
