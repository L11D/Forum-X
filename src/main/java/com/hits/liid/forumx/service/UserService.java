package com.hits.liid.forumx.service;

import com.hits.liid.forumx.entity.UserEntity;
import com.hits.liid.forumx.errors.UserAlreadyExistException;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.errors.WrongPasswordException;
import com.hits.liid.forumx.model.user.JwtResponse;
import com.hits.liid.forumx.model.user.UserLoginRequest;
import com.hits.liid.forumx.model.user.UserProfileResponse;
import com.hits.liid.forumx.model.user.UserRegistrationRequest;
import com.hits.liid.forumx.repository.UserRepository;
import com.hits.liid.forumx.utils.JwtTokenUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import java.util.UUID;


public interface UserService {
    public boolean userExist(UUID id);
    JwtResponse registerUser(@Valid UserRegistrationRequest registrationRequest);
    JwtResponse login(@Valid UserLoginRequest request);
    UserProfileResponse getProfile(Authentication authentication);
}
