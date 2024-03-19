package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.UserEntity;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.errors.UserAlreadyExistException;
import com.hits.liid.forumx.errors.WrongPasswordException;
import com.hits.liid.forumx.model.user.JwtResponse;
import com.hits.liid.forumx.model.user.UserLoginRequest;
import com.hits.liid.forumx.model.user.UserProfileResponse;
import com.hits.liid.forumx.model.user.UserRegistrationRequest;
import com.hits.liid.forumx.repository.UserRepository;
import com.hits.liid.forumx.service.UserService;
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

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final JwtTokenUtils tokenUtils;

    @SneakyThrows
    public JwtResponse registerUser(@Valid UserRegistrationRequest registrationRequest){

        if(userRepository.findByEmail(registrationRequest.email()).isPresent()){
            throw new UserAlreadyExistException(
                    String.format("User with email: '%s' already exist", registrationRequest.email())
            );
        }

        UserEntity user = UserEntity.of(null, registrationRequest.email(), registrationRequest.password(), registrationRequest.name());
        UserEntity savedUser = userRepository.save(user);
        String token = tokenUtils.generateToken(savedUser);
        return new JwtResponse(token);
    }

    @SneakyThrows
    public JwtResponse login(@Valid UserLoginRequest request){
        UserEntity user = (UserEntity) loadUserByUsername(request.email());
        if (!Objects.equals(user.getPassword(), request.password())){
            throw new WrongPasswordException("Wrong password");
        }
        String token = tokenUtils.generateToken(user);
        return new JwtResponse(token);
    }

    @SneakyThrows
    public UserProfileResponse getProfile(Authentication authentication){
        UUID userId = tokenUtils.getUserIdFromAuthentication(authentication);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return new UserProfileResponse(user.getEmail(), user.getName());
    }

    public boolean userExist(UUID id){
        return userRepository.findById(id).isPresent();
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with email: '%s' not found", email)
        ));
    }
}
