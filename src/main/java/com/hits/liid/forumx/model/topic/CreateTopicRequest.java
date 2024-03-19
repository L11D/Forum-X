package com.hits.liid.forumx.model.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTopicRequest(
        @NotBlank(message = "Topic name cannot be empty")
        @Size(min = 3, max = 100, message = "Topic name size must be between 3 and 100 characters")
        String name,
        @NotNull
        UUID categoryId
) {
}
