package com.hits.liid.forumx.controller;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.hits.liid.forumx.entity.UserEntity;
import com.hits.liid.forumx.model.user.JwtResponse;
import com.hits.liid.forumx.model.user.UserLoginRequest;
import com.hits.liid.forumx.model.user.UserProfileResponse;
import com.hits.liid.forumx.model.user.UserRegistrationRequest;
import com.hits.liid.forumx.service.UserService;
import com.hits.liid.forumx.utils.JwtTokenUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("register")
    @ResponseBody
    public JwtResponse registerUser(@RequestBody UserRegistrationRequest request){
        return userService.registerUser(request);
    }

    @GetMapping("profile")
    @ResponseBody
    public UserProfileResponse get(Authentication authentication) {
        return userService.getProfile(authentication);
    }


    @PostMapping("login")
    @ResponseBody
    public JwtResponse login(@RequestBody UserLoginRequest request){
        return userService.login(request);
    }
}
