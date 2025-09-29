package com.projects.airBnbApp.dto;

import com.projects.airBnbApp.entity.User;
import com.projects.airBnbApp.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class GuestDto {

    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
