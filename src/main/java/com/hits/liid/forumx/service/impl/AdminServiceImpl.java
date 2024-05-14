package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.CategoryEntity;
import com.hits.liid.forumx.entity.UserEntity;
import com.hits.liid.forumx.errors.AlreadyModeratorException;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.errors.NotModeratorException;
import com.hits.liid.forumx.errors.UserAlreadyExistException;
import com.hits.liid.forumx.model.UserRole;
import com.hits.liid.forumx.model.admin.CreateUserRequest;
import com.hits.liid.forumx.model.admin.EditUserRequest;
import com.hits.liid.forumx.model.user.UserRegistrationRequest;
import com.hits.liid.forumx.repository.CategoryRepository;
import com.hits.liid.forumx.repository.UserRepository;
import com.hits.liid.forumx.service.AdminService;
import com.hits.liid.forumx.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @SneakyThrows
    @Transactional
    public void createUser(@Valid CreateUserRequest request){
        userService.registerUser(new UserRegistrationRequest(
                request.email(),
                request.password(),
                request.nickname(),
                request.name(),
                request.phoneNumber()
        ));
        UserEntity createdUser = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new NotFoundException("User not found"));
        createdUser.setRole(request.role());
        userRepository.save(createdUser);
    }

    @SneakyThrows
    @Transactional
    public void editUser(@Valid @NotNull UUID userId , @Valid EditUserRequest request){
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));

        if(userRepository.findByEmail(request.email()).isPresent() && !Objects.equals(user.getEmail(), request.email())){
            throw new UserAlreadyExistException(String.format("User with email: '%s' already exist", request.email()));
        }
        if(userRepository.findByNickname(request.nickname()).isPresent() && !Objects.equals(user.getNickname(), request.nickname())){
            throw new UserAlreadyExistException(String.format("User with nickname: '%s' already exist", request.nickname()));
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        user.setEmail(request.email());
        user.setNickname(request.nickname());
        user.setPassword(encodedPassword);
        user.setName(request.name());
        user.setPhoneNumber(request.phoneNumber());
        user.setRole(request.role());
        userRepository.save(user);
    }

    @SneakyThrows
    @Transactional
    public void deleteUser(@Valid @NotNull UUID userId){
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));
        userRepository.delete(user);
    }

    @SneakyThrows
    @Transactional
    public void banUser(@Valid @NotNull UUID userId){
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }
    @SneakyThrows
    @Transactional
    public void unbanUser(@Valid @NotNull UUID userId){
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }
    @SneakyThrows
    @Transactional
    public void changeRole(@Valid @NotNull UUID userId, @Valid @NotNull UserRole role){
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));

        if (role == UserRole.MODERATOR)
            throw new NotModeratorException("The moderator role is applied automatically");

        if (role == UserRole.USER && user.getRole() == UserRole.MODERATOR)
            throw new AlreadyModeratorException("User has moderated categories");

        user.setRole(role);
        userRepository.save(user);
    }
    @SneakyThrows
    @Transactional
    public void setModeratorToCategory(@Valid @NotNull UUID userId,  @Valid @NotNull UUID categoryId){
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));

        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("Category not found"));

        if(category.getModerators().contains(user))
            throw new AlreadyModeratorException("User is already a moderator");

        category.getModerators().add(user);
        categoryRepository.save(category);
        if (user.getRole() != UserRole.ADMIN){
            user.setRole(UserRole.MODERATOR);
            userRepository.save(user);
        }
    }

    @SneakyThrows
    @Transactional
    public void deleteModeratorToCategory(@Valid @NotNull UUID userId,  @Valid @NotNull UUID categoryId){
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));

        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("Category not found"));

        if(!category.getModerators().contains(user))
            throw new NotModeratorException("User is not a moderator");

        category.getModerators().remove(user);
        categoryRepository.save(category);
        if (!userRepository.isModerator(userId) && user.getRole() != UserRole.ADMIN){
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }
    }

}
