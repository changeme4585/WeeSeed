package com.example.WeeSeed.service;

import com.example.WeeSeed.entity.Child;
import com.example.WeeSeed.repository.ChildRepo;
import com.example.WeeSeed.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;

//   public List<Child> getAllChildren() {
//        return childRepository.findAll();
//    }
    public List<Child> getChildByUser(String userId) {return childRepository.getChildByUser(userId);}

}
