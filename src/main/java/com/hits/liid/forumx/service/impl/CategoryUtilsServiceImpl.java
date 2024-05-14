package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.CategoryEntity;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.repository.CategoryRepository;
import com.hits.liid.forumx.service.CategoryUtilsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryUtilsServiceImpl implements CategoryUtilsService {
    public final CategoryRepository repository;

    @Override
    @Transactional
    public boolean categoryExist(UUID id) {
        return repository.findById(id).isPresent();
    }

    @Override
    @Transactional
    public boolean categoryHasChild(UUID id) {
        return repository.hasChildCategories(id);
    }

    @SneakyThrows
    @Transactional
    @Override
    public CategoryEntity findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Parent category not found"));
    }
}
