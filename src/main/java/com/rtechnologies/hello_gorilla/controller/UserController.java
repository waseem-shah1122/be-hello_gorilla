package com.rtechnologies.hello_gorilla.controller;


import com.rtechnologies.hello_gorilla.entity.UserEntity;
import com.rtechnologies.hello_gorilla.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<String> signUpUser(MultipartHttpServletRequest request) {
        try {
            // Extract parameters from the request
            String number = request.getParameter("number");
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String FBId = request.getParameter("FBId");
            String gender = request.getParameter("gender");
            String interest = request.getParameter("interest");

            UserEntity userEntity = new UserEntity(number,name,password,FBId,gender,interest);
            userEntity.setNumber(number);
            userEntity.setName(name);
            userEntity.setPassword(password);
            userEntity.setGender(gender);
            userEntity.setFBId(Long.parseLong(FBId));
            userEntity.setInterest(interest);

            MultipartFile profileImage = request.getFile("profileImage");
            MultipartFile idDocumentData = request.getFile("idDocument");

            try {
                userService.createUser(userEntity, profileImage, idDocumentData);
                System.out.println("userId:" + userEntity.getUserId());

                return ResponseEntity.status(HttpStatus.CREATED).body("User signed up successfully");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sign up user");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestParam String number) {
        ResponseEntity<String> userEntity = userService.logIn(number);
        if (userEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UID not found. Please sign up first.");
        }
        return ResponseEntity.ok(userEntity);
    }

    @GetMapping("detail/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Optional<UserEntity> userOptional = userService.findUserById(userId);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}






