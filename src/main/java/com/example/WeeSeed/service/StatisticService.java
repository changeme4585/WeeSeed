package com.example.WeeSeed.service;



import com.example.WeeSeed.dto.Statistic.AgeDto;
import com.example.WeeSeed.dto.Statistic.GenderDto;
import com.example.WeeSeed.dto.Statistic.GradeDto;
import com.example.WeeSeed.dto.Statistic.TypeDto;
import com.example.WeeSeed.dto.StatisticDto;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.videoCard;
import com.example.WeeSeed.repository.AacRepository;
import com.example.WeeSeed.repository.ChildRepository;
import com.example.WeeSeed.repository.StatisticRepository;
import com.example.WeeSeed.repository.VideoCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository statisticRepository;

    private  final AacRepository aacRepository;

    private  final VideoCardRepository videoCardRepository;

    private  final ChildRepository childRepository;
    @Value("${raspberry.pi.url}")
    private String raspberryPiUrl;


    public  int calculateAge(String birthdayStr) {
        // 날짜 포맷터 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        // 문자열을 LocalDate 객체로 변환
        LocalDate birthday = LocalDate.parse(birthdayStr, formatter);
        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();

        // Period 객체를 사용하여 나이 계산
        Period age = Period.between(birthday, today);

        return age.getYears();
    }

    public AgeDto ageStatistic(){
        List<AacCard> aacCards = aacRepository.getAllAacCard() ;
        List<videoCard> videoCards = videoCardRepository.getAllVideoCard() ;
        int zeroAac = 0;
        int fiveAac = 0;
        int tenAac = 0;
        int fifteenAac = 0;

        for(int i = 0 ;i<aacCards.size();i++){

            String childBirth = childRepository.getChildBirth(aacCards.get(i).getChildId());


            int childAge = calculateAge(childBirth);

            if (0<=childAge && childAge<5){
                zeroAac = childAge;
            }
            else if (5<=childAge && childAge<10){
                fiveAac = childAge;
            }
            else if (10<=childAge && childAge<15){
                tenAac = childAge;
            }
            else if (childAge>=15){
                fifteenAac = childAge;
            }
        }
        int zeroVideo = 0 ;
        int fiveVideo = 0;
        int tenVideo = 0 ;
        int fifteenVideo = 0;
        for(int i = 0 ;i<videoCards.size();i++){
            String childBirth = childRepository.getChildBirth(videoCards.get(i).getChildId());

            System.out.println("아동 출력오류: "+childBirth);

            int childAge = calculateAge(childBirth);

            if (0<=childAge && childAge<5){
                zeroVideo = childAge;
            }
            else if (5<=childAge && childAge<10){
                fiveVideo = childAge;
            }
            else if (10<=childAge && childAge<15){
                tenVideo = childAge;
            }
            else if (childAge>=15){
                fifteenVideo = childAge;
            }
        }

        AgeDto ageDto = AgeDto.
                builder().
                zeroAac(zeroAac).
                fiveAac(fiveAac).
                tenAac(tenAac).
                fifteenAac(fifteenAac).
                zeroVideo(zeroVideo).
                fiveVideo(fiveVideo).
                tenVideo(tenVideo).
                fifteenVideo(fifteenVideo).
                build();

        return ageDto;
    }

    public GenderDto genderStatistic(){
        List<AacCard> maleAac = statisticRepository.getAaCardGender("M");
        List<AacCard> femaleAac = statisticRepository.getAaCardGender("F");

        List<videoCard> maleVideo = statisticRepository.getVideoCardGender("M");
        List<videoCard> femaleVideo = statisticRepository.getVideoCardGender("F");
        GenderDto genderDto = GenderDto.builder().
                maleAac(maleAac.size()).
                femaleAac(femaleAac.size()).
                maleVideo(maleVideo.size()).
                femaleVideo(femaleVideo.size()).
                build();
        return genderDto;
    }


//    public AgeDto ageStatistic (){
//
//
//        return ageDto ;
//    }
    public StatisticDto getDateStatistic(int num,String childId,String userId){
        System.out.println(num+"일 통계");
        List<AacCard> aacCardList =statisticRepository.getAacCard(childId,userId);
        List<videoCard> videoCardList = statisticRepository.getVideoCard(childId,userId);
        int aacIdx = 0 ;
        int videoIdx = 0 ;
        int imageCardNum = aacCardList.size();
        int videoCardNum = videoCardList.size();
        String cardName = "";
        String image = "";
        String color = "";
        int maxCnt  = 0 ;
        String aacDate = "";
        String videoDate = "";
        if(imageCardNum != 0) {
            aacDate = aacCardList.get(0).getCreationTime();
        }
        if(videoCardNum != 0) {
            videoDate = videoCardList.get(0).getCreationTime();
        }

        int aacDateCnt = 0;
        int videoDateCnt = 0;
        while (aacIdx < aacCardList.size()  && aacDateCnt < num){
            System.out.println("AAC 카드 개수: "+aacCardList.get(aacIdx).getClickCnt());
            if(maxCnt <aacCardList.get(aacIdx).getClickCnt()){
                cardName = aacCardList.get(aacIdx).getCardName();
                image = aacCardList.get(aacIdx).getImageUrl() ;
                color = aacCardList.get(aacIdx).getColor();
                maxCnt = aacCardList.get(aacIdx).getClickCnt();
            }


            if(!aacDate.equals(aacCardList.get(aacIdx).getCreationTime()) ){
                aacDate = aacCardList.get(aacIdx).getCreationTime();
                aacDateCnt+=1;
            }
            aacIdx+=1;
        }
        while(videoIdx < videoCardList.size() && videoDateCnt < num){
            System.out.println("비디오 카드 개수: "+videoCardList.get(videoIdx).getClickCnt());
            if(maxCnt < videoCardList.get(videoIdx).getClickCnt()){

                cardName = videoCardList.get(videoIdx).getCardName();
                image = videoCardList.get(videoIdx).getThumbnailUrl();
                color = videoCardList.get(videoIdx).getColor();
                maxCnt = videoCardList.get(videoIdx).getClickCnt();
            }

            if(!videoDate.equals(videoCardList.get(videoIdx).getCreationTime())){
                videoDate = videoCardList.get(videoIdx).getCreationTime();
                videoDateCnt+=1;
            }
            videoIdx+=1;
        }

        StatisticDto statisticDto = StatisticDto.builder().
                imageCardNum(imageCardNum).
                videoCardNum(videoCardNum).
                cardName(cardName).
                image(raspberryPiUrl+image).
                color(color).
                build();

        return statisticDto;
    }
    public List<StatisticDto> getPersonalStatistic(String childId,String userId){
        List<StatisticDto> statisticDtoList = new ArrayList<>();
        statisticDtoList.add(getDateStatistic(1,childId,userId));
        statisticDtoList.add(getDateStatistic(7,childId,userId));
        statisticDtoList.add(getDateStatistic(30,childId,userId));
        return statisticDtoList;
    }
    public TypeDto disabilityType(){
        TypeDto typeDto=  TypeDto.builder().
                autism(Math.toIntExact(statisticRepository.getTypeNum("자폐성장애"))).
                intellectual(Math.toIntExact(statisticRepository.getTypeNum("지적장애"))).
                behavioral(Math.toIntExact(statisticRepository.getTypeNum("행동장애"))).
                pronunciation(Math.toIntExact(statisticRepository.getTypeNum("발음장애"))).
                brainLesion(Math.toIntExact(statisticRepository.getTypeNum("뇌병변장애"))).
                build();
        return  typeDto;
    }

    public GradeDto disabilityGrade(){
        GradeDto gradeDto = GradeDto.builder().
                oneAac(Math.toIntExact(statisticRepository.getGradeNum("1"))).
                twoAac(Math.toIntExact(statisticRepository.getGradeNum("2"))).
                threeAac(Math.toIntExact(statisticRepository.getGradeNum("3"))).
                fourAac(Math.toIntExact(statisticRepository.getGradeNum("4"))).
                fiveAac(Math.toIntExact(statisticRepository.getGradeNum("5"))).
                sixAac(Math.toIntExact(statisticRepository.getGradeNum("6"))).
                build();
        return gradeDto;
    }
}
