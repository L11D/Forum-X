package com.hits.liid.forumx.service;

import com.hits.liid.forumx.model.SortingValues;
import com.hits.liid.forumx.model.category.*;
import com.hits.liid.forumx.model.message.MessageDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;


public interface CategoryService {
    List<CategorySearchDTO> searchSubstring(@Valid @NotBlank String substring);
    List<CategoryDTO> getCategories(SortingValues sorting);
    UUID create(@Valid CreateCategoryRequest request, Authentication authentication);
    void edit(@Valid EditCategoryRequest request, UUID categoryId);
    void delete(UUID categoryId, boolean chainDelete);
}
