package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.CategoryEntity;
import com.hits.liid.forumx.entity.TopicEntity;
import com.hits.liid.forumx.entity.UserEntity;
import com.hits.liid.forumx.errors.HasChildException;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.model.SortingValues;
import com.hits.liid.forumx.model.category.*;
import com.hits.liid.forumx.repository.CategoryRepository;
import com.hits.liid.forumx.service.CategoryService;
import com.hits.liid.forumx.service.CategoryUtilsService;
import com.hits.liid.forumx.service.TopicService;
import com.hits.liid.forumx.service.UserService;
import com.hits.liid.forumx.utils.JwtTokenUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    public final CategoryRepository categoryRepository;
    private final JwtTokenUtils tokenUtils;
    private final TopicService topicService;
    private final CategoryUtilsService categoryUtilsService;
    private final UserService userService;

    @Override
    @Transactional
    public List<CategorySearchDTO> searchSubstring(@Valid @NotBlank String substring) {
        List<CategoryEntity> categoriesEntity = categoryRepository.findBySubstring(substring);
        List<CategorySearchDTO> categories = new ArrayList<>();
        for (CategoryEntity c : categoriesEntity){
            categories.add(new CategorySearchDTO(
                    c.getId(),
                    c.getName(),
                    c.getParentCategory() != null ? c.getParentCategory().getId() : null,
                    c.getUserCreator() != null ? c.getUserCreator().getId() : null,
                    c.getUserCreator() != null ? c.getUserCreator().getNickname() : null,
                    c.getCreationDate(),
                    c.getEditingDate()
            ));
        }
        return categories;
    }

    public List<CategoryDTO> getCategories(SortingValues sorting){
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        switch (sorting){
            case NAME_ASC -> {
                sort = Sort.by(Sort.Direction.ASC, "name");
            }
            case NAME_DESC -> {
                sort = Sort.by(Sort.Direction.DESC, "name");
            }
            case CREATION_DATE_ASC -> {
                sort = Sort.by(Sort.Direction.ASC, "creationDate");
            }
            case CREATION_DATE_DESC -> {
                sort = Sort.by(Sort.Direction.DESC, "creationDate");
            }
        }

        return findChild(null, sort);
    }
    @Transactional
    protected List<CategoryDTO> findChild(UUID parentID, Sort sort){
        List<CategoryEntity> child;
        if (parentID == null){
            child = categoryRepository.findTopCategories(sort);
        }
        else
        {
           child = categoryRepository.findChildCategories(parentID, sort);
        }

        List<CategoryDTO> ans = new ArrayList<>();
        for (CategoryEntity e : child){
            ans.add(new CategoryDTO(
                    e.getId(),
                    e.getName(),
                    e.getUserCreator() != null ? e.getUserCreator().getId() : null,
                    e.getUserCreator() != null ? e.getUserCreator().getNickname() : null,
                    e.getCreationDate(),
                    e.getEditingDate(),
                    findChild(e.getId(), sort)
            ));
        }
        return ans;
    }

    @SneakyThrows
    @Transactional
    public UUID create(@Valid CreateCategoryRequest request, Authentication authentication){
        UUID userId = tokenUtils.getUserIdFromAuthentication(authentication);
        UserEntity user = userService.getUserById(userId);
        CategoryEntity parentCategory = null;
        if (request.parentCategoryId() != null){
            parentCategory = categoryRepository.findById(request.parentCategoryId()).orElseThrow(() ->
                    new NotFoundException("Parent category not found"));
        }

        CategoryEntity category = CategoryEntity.of(
                null,
                user,
                LocalDateTime.now(),
                null,
                request.name(),
                parentCategory);
        CategoryEntity createdCategory = categoryRepository.save(category);
        return createdCategory.getId();
    }

    @SneakyThrows
    @Transactional
    @PreAuthorize("@categoryServiceImpl.checkPermission(#categoryId, authentication) || hasRole('ADMIN')")
    public void edit(@Valid EditCategoryRequest request, UUID categoryId){
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Category not found"));

        CategoryEntity parentCategory = null;
        if (request.parentCategoryId() != null){
            parentCategory = categoryRepository.findById(request.parentCategoryId()).orElseThrow(() ->
                    new NotFoundException("Parent category not found"));
        }

        category.setParentCategory(parentCategory);
        category.setName(request.name());
        category.setEditingDate(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @SneakyThrows
    @Transactional
    @PreAuthorize("@categoryServiceImpl.checkPermission(#categoryId, authentication) || hasRole('ADMIN')")
    public void delete(UUID categoryId, boolean chainDelete){
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Category not found"));

        if (chainDelete){
            List<CategoryEntity> childCategories = categoryRepository.findChildCategories(categoryId);
            for (CategoryEntity child : childCategories) {
                delete(child.getId(), true);
            }
            List<TopicEntity> topics = categoryRepository.findTopicsByCategoryId(categoryId);
            for (TopicEntity topic : topics) {
                topicService.delete(topic.getId());
            }
        }

        if (categoryRepository.hasChildCategories(categoryId)){
            throw new HasChildException("Сannot delete a category has child elements. Use chainDelete = true");
        }

        if (categoryRepository.hasTopics(categoryId)){
            throw new HasChildException("Сannot delete a category has topics. Use chainDelete = true");
        }

        categoryRepository.delete(category);
    }

    @SneakyThrows
    @Transactional
    public boolean checkPermission(UUID categoryId, Authentication authentication){
        UUID userId = tokenUtils.getUserIdFromAuthentication(authentication);

        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Category not found"));

        return userId.equals(category.getUserCreator().getId());
    }
}
