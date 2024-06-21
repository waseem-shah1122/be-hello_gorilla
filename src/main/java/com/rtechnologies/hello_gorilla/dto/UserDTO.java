package com.rtechnologies.hello_gorilla.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {


    private Long userId;
    private String number;
    private String name;
    private String password;
    private String gender;
    private Long FBId;
    private String interest;
    private String profileImage;
    private String idDocumentData;
}
