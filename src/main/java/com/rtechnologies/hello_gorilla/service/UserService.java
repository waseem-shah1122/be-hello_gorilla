package com.rtechnologies.hello_gorilla.service;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rtechnologies.hello_gorilla.controller.UserController;
import com.rtechnologies.hello_gorilla.dto.ResponseMessage;
import com.rtechnologies.hello_gorilla.entity.UserEntity;
import com.rtechnologies.hello_gorilla.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Cloudinary cloudinary;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);



    public UserEntity createUser(UserEntity user, MultipartFile profileImage, MultipartFile idDocument) throws IOException {
        // Upload profile image to Cloudinary and get the URL
        if (profileImage != null && !profileImage.isEmpty()) {
            String folder = "profile-pics";
            String publicId = folder + "/" + profileImage.getOriginalFilename();
            Map uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), ObjectUtils.asMap("public_id", publicId));
            String profilePicUrl = uploadResult.get("secure_url").toString();
            user.setProfileImage(profilePicUrl);
        }

        if (idDocument != null && !idDocument.isEmpty()) {
            String folder = "profile-pics";
            String publicId = folder + "/" + idDocument.getOriginalFilename();
            Map uploadResult = cloudinary.uploader().upload(idDocument.getBytes(), ObjectUtils.asMap("public_id", publicId));
            String profilePicUrl = uploadResult.get("secure_url").toString();
            user.setDocument(profilePicUrl);
        }

        return userRepository.save(user);
    }


    public ResponseEntity<ResponseMessage>logIn(String number) {


        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userRepository.findByNumber(number));

//        if (!userEntityOptional.isPresent()) {
//            return ResponseEntity.ok(new ResponseMessage(false, "invalid number"));
//        }
        return ResponseEntity.ok(new ResponseMessage(true, "User logged In successfully"));

    }

    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);


        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);


        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // Distance in km
    }

    public List<UserEntity> findUsersWithinRadius(double latitude, double longitude) {

        List<UserEntity> allUsers = userRepository.findAll();

        return allUsers.stream()
                .filter(user ->
                {
                    Double userLatitude = user.getLatitude();
                    Double userLongitude = user.getLongitude();
                    if (userLatitude == null || userLongitude == null) {
                        logger.warn("User {} has null latitude or longitude", user.getUserId());
                        return false;
                    }
                    return calculateDistance(latitude, longitude, userLatitude, userLongitude) <= 5;
                })
                .collect(Collectors.toList());
    }

    public List<UserEntity> filterUsers(Optional<String> name, Optional<String> gender, Optional<String> interest,
                                        Optional<Double> latitude, Optional<Double> longitude, Optional<Double> radius) {
        List<UserEntity> allUsers = userRepository.findAll();

        logger.info("Filtering users with the following criteria - name: {}, gender: {}, interest: {}, latitude: {}, longitude: {}, radius: {}",
                name.orElse("any"), gender.orElse("any"), interest.orElse("any"), latitude.orElse(null), longitude.orElse(null), radius.orElse(null));

        List<UserEntity> filteredUsers = allUsers.stream()
                .filter(user -> name.map(n -> user.getName().equalsIgnoreCase(n)).orElse(true))
                .filter(user -> gender.map(g -> user.getGender().equalsIgnoreCase(g)).orElse(true))
                .filter(user -> interest.map(i -> user.getInterest().equalsIgnoreCase(i)).orElse(true))
                .filter(user -> {
                    if (latitude.isPresent() && longitude.isPresent() && radius.isPresent()) {
                        Double userLatitude = user.getLatitude();
                        Double userLongitude = user.getLongitude();
                        if (userLatitude == null || userLongitude == null) {
                            logger.info("User {} does not have valid latitude/longitude", user.getUserId());
                            return false;
                        }
                        double distance = distanceCalculate(latitude.get(), longitude.get(), userLatitude, userLongitude);
                        logger.info("User {} is at distance: {} km", user.getUserId(), distance);

                        return distance <= radius.get();
                    } else {

                        return true;
                    }
                })
                .collect(Collectors.toList());
        logger.info("Found {} users after filtering", filteredUsers.size());
        return filteredUsers;
    }

    private double distanceCalculate(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // Distance in km
    }


    }

