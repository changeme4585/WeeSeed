package com.example.WeeSeed.controller;

import com.example.WeeSeed.entity.Child;
import com.example.WeeSeed.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChildController {


    private final  ChildService childService;

    @GetMapping(value= "/NokChildInfo/{nokId}")
    public ResponseEntity<List<Child>> NokChildInfo(@PathVariable("nokId")String nokId) {
        List<Child> children = childService.getAllChildren();
        System.out.println("nokId: "+ nokId);
        System.out.println("아이 이름+ "+ children.get(0).getName());
        return new ResponseEntity<>(children, HttpStatus.OK);
    }

    @GetMapping(value= "/PathChildInfo/{pathId}")
    public ResponseEntity<List<Child>> PathChildInfo(@PathVariable("pathId")String pathId) {
        List<Child> children = childService.getAllChildren();
        return new ResponseEntity<>(children, HttpStatus.OK);
    }
}