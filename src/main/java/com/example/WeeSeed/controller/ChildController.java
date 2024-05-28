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


    private ChildService childService;

    @GetMapping(value= "/childInfo")
    public ResponseEntity<List<Child>> getAllChildren() {
        List<Child> children = childService.getAllChildren();
        return new ResponseEntity<>(children, HttpStatus.OK);
    }
}