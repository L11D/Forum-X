package com.hits.liid.forumx.model.admin;

import com.hits.liid.forumx.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest (
        @NotBlank(message = "User email cannot be empty")
        @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "Email is not valid")
        String email,
        @Size(min = 6, message = "Password must be at least 6 character")
        @NotBlank(message = "User password cannot be empty")
        String password,
        @Size(min = 3, message = "Nickname must be at least 3 character")
        @NotBlank(message = "User nickname cannot be empty")
        String nickname,
        @NotBlank(message = "User name cannot be empty")
        @Size(min = 3, max = 50, message = "User name size must be between 3 and 50 characters")
        String name,
        @Size(min = 11, max = 12, message = "User phone number size must be between 11 and 12 characters")
        String phoneNumber,
        @NotNull
        UserRole role
) {
}
