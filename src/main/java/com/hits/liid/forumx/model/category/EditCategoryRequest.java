package com.hits.liid.forumx.model.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EditCategoryRequest(
        @NotBlank(message = "Category name cannot be empty")
        @Size(min = 3, max = 100, message = "Category name size must be between 3 and 100 characters")
        String name,
        UUID parentCategoryId
){

}
