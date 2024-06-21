package com.rtechnologies.hello_gorilla.controller;


import com.rtechnologies.hello_gorilla.entity.MeetUpUserEntity;
import com.rtechnologies.hello_gorilla.service.MeetUpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class MeetUpUserController {

    @Autowired
    private MeetUpUserService meetUpUserService;


    @GetMapping("/meetup")
    public List<MeetUpUserEntity> getUsersWithin5km(@RequestParam double latitude, @RequestParam double longitude) {
        return meetUpUserService.findNearbyUsers(latitude, longitude, 5.0);
    }
}
