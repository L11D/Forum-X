package com.hits.liid.forumx.model.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateMessageRequest(
        @NotBlank(message = "Message text cannot be empty")
        @Size(min = 1, max = 10000, message = "Message text size must be between 1 and 10000 characters")
        String text,
        @NotNull
        UUID topicId
) {
}
