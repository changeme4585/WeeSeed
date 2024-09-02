package com.example.WeeSeed.controller;


import com.example.WeeSeed.service.FindUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindUserInfoController {
    private final FindUserService findUserService;
    @GetMapping (value =  "/find-user-id")
    public ResponseEntity<String> findUserId(@RequestParam("name") String name,
                                            @RequestParam("email") String email){

        return new  ResponseEntity<>(findUserService.findUserId(name,email),HttpStatus.OK);

    }
}
