package com.hits.liid.forumx.service;

import com.hits.liid.forumx.model.UserRole;
import com.hits.liid.forumx.model.admin.CreateUserRequest;
import com.hits.liid.forumx.model.admin.EditUserRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface AdminService {
    void createUser(@Valid CreateUserRequest request);
    void editUser(@Valid @NotNull UUID userId , @Valid EditUserRequest request);
    void deleteUser(@Valid @NotNull UUID userId);
    void banUser(@Valid @NotNull UUID userId);
    void unbanUser(@Valid @NotNull UUID userId);
    void changeRole(@Valid @NotNull UUID userId, @Valid @NotNull UserRole role);
    void setModeratorToCategory(@Valid @NotNull UUID userId,  @Valid @NotNull UUID categoryId);
    void deleteModeratorToCategory(@Valid @NotNull UUID userId,  @Valid @NotNull UUID categoryId);
}
