package com.rtechnologies.hello_gorilla.service;

import com.rtechnologies.hello_gorilla.entity.MeetUpUserEntity;
import com.rtechnologies.hello_gorilla.repository.MeetUpUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeetUpUserService {
    @Autowired
    private MeetUpUserRepository meetUpUserRepository;

    private static final int EARTH_RADIUS_KM = 6371;


        public List<MeetUpUserEntity> findNearbyUsers(double latitude, double longitude, double radius) {
            List<MeetUpUserEntity> allUsers = meetUpUserRepository.findAll();
            List<MeetUpUserEntity> nearbyUsers = new ArrayList<>();

            for (MeetUpUserEntity user : allUsers) {
                double distance = calculateDistance(latitude, longitude, user.getLatitude(), user.getLongitude());
                if (distance <= radius) {
                    nearbyUsers.add(user);
                }
            }

            return nearbyUsers;
        }


    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
    }

