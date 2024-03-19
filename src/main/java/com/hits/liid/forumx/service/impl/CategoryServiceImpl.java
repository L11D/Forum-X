package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.CategoryEntity;
import com.hits.liid.forumx.entity.MessageEntity;
import com.hits.liid.forumx.entity.TopicEntity;
import com.hits.liid.forumx.errors.HasChildException;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.model.SortingValues;
import com.hits.liid.forumx.model.category.*;
import com.hits.liid.forumx.model.message.MessageDTO;
import com.hits.liid.forumx.repository.CategoryRepository;
import com.hits.liid.forumx.service.CategoryService;
import com.hits.liid.forumx.service.CategoryUtilsService;
import com.hits.liid.forumx.service.TopicService;
import com.hits.liid.forumx.utils.JwtTokenUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
    public final CategoryRepository repository;
    private final JwtTokenUtils tokenUtils;
    private final TopicService topicService;
    private final CategoryUtilsService categoryUtilsService;

    @Override
    public List<CategorySearchDTO> searchSubstring(@Valid @NotBlank String substring) {
        List<CategoryEntity> categoriesEntity = repository.findBySubstring(substring);
        List<CategorySearchDTO> categories = new ArrayList<>();
        for (CategoryEntity c : categoriesEntity){
            categories.add(new CategorySearchDTO(
                    c.getId(),
                    c.getName(),
                    c.getUserCreatorId(),
                    c.getParentCategoryId(),
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

    private List<CategoryDTO> findChild(UUID parentID, Sort sort){
        List<CategoryEntity> child;
        if (parentID == null){
            child = repository.findTopCategories(sort);
        }
        else
        {
           child = repository.findChildCategories(parentID, sort);
        }

        List<CategoryDTO> ans = new ArrayList<>();
        for (CategoryEntity e : child){
            ans.add(new CategoryDTO(
                    e.getId(),
                    e.getName(),
                    e.getUserCreatorId(),
                    e.getCreationDate(),
                    e.getEditingDate(),
                    findChild(e.getId(), sort)
            ));
        }
        return ans;
    }

    @SneakyThrows
    public UUID create(@Valid CreateCategoryRequest request, Authentication authentication){
        UUID userId = tokenUtils.getUserIdFromAuthentication(authentication);

        if (request.parentCategoryId() != null){
            repository.findById(request.parentCategoryId()).orElseThrow(() ->
                    new NotFoundException("Parent category not found"));
        }

        CategoryEntity category = CategoryEntity.of(
                null,
                userId,
                LocalDateTime.now(),
                null,
                request.name(),
                request.parentCategoryId());
        CategoryEntity createdCategory = repository.save(category);
        return createdCategory.getId();
    }

    @SneakyThrows
//    @Transactional
    public void edit(@Valid EditCategoryRequest request, UUID categoryId){
        CategoryEntity category = repository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Category not found"));

        if (request.parentCategoryId() != null){
            if(!categoryUtilsService.categoryExist(request.parentCategoryId())){
                throw new NotFoundException("Parent category not found");
            }
        }

        category.setParentCategoryId(request.parentCategoryId());
        category.setName(request.name());
        category.setEditingDate(LocalDateTime.now());
        repository.save(category);
    }

    @SneakyThrows
    public void delete(UUID categoryId, boolean chainDelete){
        CategoryEntity category = repository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Category not found"));

        if (chainDelete){
            List<CategoryEntity> childCategories = repository.findChildCategories(categoryId);
            for (CategoryEntity child : childCategories) {
                delete(child.getId(), true);
            }
            List<TopicEntity> topics = repository.findTopicsByCategoryId(categoryId);
            for (TopicEntity topic : topics) {
                topicService.delete(topic.getId());
            }
        }

        if (repository.hasChildCategories(categoryId)){
            throw new HasChildException("Сannot delete a category has child elements. Use chainDelete = true");
        }

        if (repository.hasTopics(categoryId)){
            throw new HasChildException("Сannot delete a category has topics. Use chainDelete = true");
        }

        repository.delete(category);
    }
}
