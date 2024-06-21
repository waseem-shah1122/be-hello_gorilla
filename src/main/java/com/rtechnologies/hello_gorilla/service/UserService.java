package com.rtechnologies.hello_gorilla.service;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rtechnologies.hello_gorilla.entity.UserEntity;
import com.rtechnologies.hello_gorilla.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Cloudinary cloudinary;




    public UserEntity createUser(UserEntity user, MultipartFile profileImage, MultipartFile idDocument) throws IOException {
        // Upload profile image to Cloudinary and get the URL
        if (profileImage != null && !profileImage.isEmpty()) {
            String folder = "profile-pics";
            String publicId = folder + "/" + profileImage.getOriginalFilename();
            Map uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), ObjectUtils.asMap("public_id", publicId));
            String profilePicUrl = uploadResult.get("secure_url").toString();
            user.setProfileImage(profilePicUrl);  // Store the URL instead of base64 encoded string
        }

        if (idDocument != null && !idDocument.isEmpty()) {
            String folder = "profile-pics";
            String publicId = folder + "/" + idDocument.getOriginalFilename();
            Map uploadResult = cloudinary.uploader().upload(idDocument.getBytes(), ObjectUtils.asMap("public_id", publicId));
            String profilePicUrl = uploadResult.get("secure_url").toString();
            user.setDocument(profilePicUrl);  // Store the URL instead of base64 encoded string
        }

        // Save the user entity to the database
        return userRepository.save(user);
    }


    public ResponseEntity<String> logIn(String number) {
        userRepository.findByNumber(number);
        return ResponseEntity.status(HttpStatus.OK).body("User loged In successfully");

    }

    public Optional<UserEntity> findUserById(Long userId) {
        
        return userRepository.findById(userId);
    }
}
