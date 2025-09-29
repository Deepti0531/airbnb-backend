package com.projects.airBnbApp.service;

import com.projects.airBnbApp.dto.ProfileUpdateRequestDto;
import com.projects.airBnbApp.dto.UserDto;
import com.projects.airBnbApp.entity.User;
import com.projects.airBnbApp.exception.ResourceNotFoundException;
import com.projects.airBnbApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.projects.airBnbApp.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }

    // New method: get my profile
    @Override
    public UserDto getMyProfile() {
        User user = getCurrentUser();
        return modelMapper.map(user, UserDto.class);
    }

    // New method: update my profile
    @Override
    public void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {
        User user = getCurrentUser();

        if (profileUpdateRequestDto.getName() != null) {
            user.setName(profileUpdateRequestDto.getName());
        }
        if (profileUpdateRequestDto.getDateOfBirth() != null) {
            user.setDateOfBirth(profileUpdateRequestDto.getDateOfBirth());
        }
        if (profileUpdateRequestDto.getGender() != null) {
            user.setGender(profileUpdateRequestDto.getGender());
        }

        userRepository.save(user);
    }

}
