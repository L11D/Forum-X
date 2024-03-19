package com.hits.liid.forumx.model.user;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
public record UserRegistrationRequest(
        @NotBlank(message = "User email cannot be empty")
        @Pattern(regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$", message = "Email is not valid")
        String email,
        @Size(min = 6, message = "Password must be at least 6 character")
        @NotBlank(message = "User password cannot be empty")
        String password,
        @NotBlank(message = "User name cannot be empty")
        @Size(min = 3, max = 50, message = "User name size must be between 3 and 50 characters")
        String name
        )
{

}

