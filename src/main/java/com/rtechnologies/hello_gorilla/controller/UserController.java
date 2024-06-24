package com.rtechnologies.hello_gorilla.controller;


import com.rtechnologies.hello_gorilla.dto.ResponseMessage;
import com.rtechnologies.hello_gorilla.entity.UserEntity;
import com.rtechnologies.hello_gorilla.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signUpUser(MultipartHttpServletRequest request) {
        try {
            // Extract parameters from the request
            String number = request.getParameter("number");
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String FBId = request.getParameter("FBId");
            String gender = request.getParameter("gender");
            String interest = request.getParameter("interest");
            double latitude = Double.parseDouble(request.getParameter("latitude"));
            double longitude = Double.parseDouble(request.getParameter("longitude"));
            double radius = Double.parseDouble(request.getParameter("radius"));

            UserEntity userEntity = new UserEntity(number,name,password,FBId,gender,interest,latitude,longitude,radius);
            userEntity.setNumber(number);
            userEntity.setName(name);
            userEntity.setPassword(password);
            userEntity.setGender(gender);
            userEntity.setFBId(Long.parseLong(FBId));
            userEntity.setInterest(interest);
            userEntity.setLatitude(latitude);
            userEntity.setLongitude(longitude);
            userEntity.setRadius(radius);

            MultipartFile profileImage = request.getFile("profileImage");
            MultipartFile idDocumentData = request.getFile("idDocument");

            try {
                userService.createUser(userEntity, profileImage, idDocumentData);

                return ResponseEntity.ok(new ResponseMessage(true, "User signed up successfully"));
            } catch (IOException e) {
                return ResponseEntity.ok(new ResponseMessage(false, "Failed to sign up user"));
            }
        } catch (Exception e) {
            return  ResponseEntity.ok(new ResponseMessage(false, "number already exist"));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseMessage> signIn(@RequestParam String number) {
        userService.logIn(number);
        return ResponseEntity.ok(new ResponseMessage(true, "User signed up successfully"));
    }
    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) Long userId) {
        if (userId == null) {
            List<UserEntity> users =
                    userService.findAllUsers();
            return ResponseEntity.ok(users);
        }
        else {
            Optional<UserEntity> user = userService.findUserById(userId);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
    }


    @GetMapping("/nearby")
    public ResponseEntity<ResponseMessage> findUsersWithinRadius(@RequestParam Double latitude, @RequestParam Double longitude) {
        List<UserEntity> users = userService.findUsersWithinRadius(latitude, longitude);
        if (users.isEmpty()) {
            logger.info("No users found within the specified radius");
            return  ResponseEntity.ok(new ResponseMessage(false,"No User"));
        } else {
            logger.info("Found {} users within the specified radius", users.size());
            return ResponseEntity.ok(new ResponseMessage(true, "Users found"));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseMessage> filterUsers(
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> gender,
            @RequestParam Optional<String> interest,
            @RequestParam Optional<Double> latitude,
            @RequestParam Optional<Double> longitude,
            @RequestParam Optional<Double> radius) {

//        logger.info("Received filter request with - name: {}, gender: {}, interest: {}, latitude: {}, longitude: {}, radius: {}",
//                name.orElse("any"), gender.orElse("any"), interest.orElse("any"), latitude.orElse(null), longitude.orElse(null), radius.orElse(null));


        List<UserEntity> users = userService.filterUsers(name, gender, interest, latitude, longitude, radius);
        if (users.isEmpty()) {
            logger.info("No users found within the specified criteria");
            return ResponseEntity.ok(new ResponseMessage(false, "Users not found"));
        } else {
            logger.info("Found {} users within the specified criteria", users.size());
            return ResponseEntity.ok(new ResponseMessage(true, "Users found"));
        }
    }
}






