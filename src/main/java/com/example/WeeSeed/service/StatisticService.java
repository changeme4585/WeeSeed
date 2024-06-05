package com.example.WeeSeed.service;


import com.example.WeeSeed.dto.AacDto;
import com.example.WeeSeed.dto.StatisticDto;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticRepository statisticRepository;
//    public List<AacCard> getDateStatistic(int num,String childId,String userId){
//        List<AacCard> list =statisticRepository.getAacCard()
//        List<AacCard> ans =
//        for(int i = 0)
//        return list;
//    }
//    public StatisticDto getPersonalStatistic(){
//
//        return
//    }
}
