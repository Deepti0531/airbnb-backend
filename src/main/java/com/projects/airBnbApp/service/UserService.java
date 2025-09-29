package com.projects.airBnbApp.service;

import com.projects.airBnbApp.dto.ProfileUpdateRequestDto;
import com.projects.airBnbApp.dto.UserDto;
import com.projects.airBnbApp.entity.User;

public interface UserService {
    User getUserById(Long id);

    // New methods
    UserDto getMyProfile();
    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);
}
