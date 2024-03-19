package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.repository.CategoryRepository;
import com.hits.liid.forumx.service.CategoryUtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryUtilsServiceImpl implements CategoryUtilsService {
    public final CategoryRepository repository;

    @Override
    public boolean categoryExist(UUID id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public boolean categoryHasChild(UUID id) {
        return repository.hasChildCategories(id);
    }
}
