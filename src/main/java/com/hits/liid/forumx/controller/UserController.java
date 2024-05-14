package com.hits.liid.forumx.controller;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.hits.liid.forumx.entity.UserEntity;
import com.hits.liid.forumx.model.UserRole;
import com.hits.liid.forumx.model.admin.CreateUserRequest;
import com.hits.liid.forumx.model.admin.EditUserRequest;
import com.hits.liid.forumx.model.user.JwtResponse;
import com.hits.liid.forumx.model.user.UserLoginRequest;
import com.hits.liid.forumx.model.user.UserProfileResponse;
import com.hits.liid.forumx.model.user.UserRegistrationRequest;
import com.hits.liid.forumx.service.AdminService;
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
    private final AdminService adminService;
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

    @PostMapping("login/{refreshToken}")
    @ResponseBody
    public JwtResponse loginWithRefreshToken(@PathVariable("refreshToken") UUID refreshToken){
        return userService.loginWithAccessToken(refreshToken);
    }

    @PostMapping("admin/create")
    public void createUser(@RequestBody CreateUserRequest request){
        adminService.createUser(request);
    }

    @PostMapping("admin/{userId}/edit")
    public void editUser( @PathVariable UUID userId, @RequestBody EditUserRequest request){
        adminService.editUser(userId, request);
    }

    @DeleteMapping("admin/{userId}/delete")
    public void deleteUser( @PathVariable UUID userId){
        adminService.deleteUser(userId);
    }

    @PostMapping("admin/{userId}/ban")
    public void banUser(@PathVariable UUID userId){
        adminService.banUser(userId);
    }
    @PostMapping("admin/{userId}/unban")
    public void unbanUser(@PathVariable UUID userId){
        adminService.unbanUser(userId);
    }
    @PostMapping("admin/{userId}/changeRole")
    public void changeRole(@PathVariable UUID userId,
                           @RequestParam UserRole role){
        adminService.changeRole(userId, role);
    }
    @PostMapping("admin/{userId}/setModeratorToCategory")
    public void setModeratorToCategory(@PathVariable UUID userId,
                                       @RequestParam UUID categoryId){
        adminService.setModeratorToCategory(userId, categoryId);
    }
    @DeleteMapping("admin/{userId}/setModeratorToCategory")
    public void deleteModeratorToCategory(@PathVariable UUID userId,
                                          @RequestParam UUID categoryId){
        adminService.deleteModeratorToCategory(userId, categoryId);
    }
}
