package com.hits.liid.forumx.service;

import com.hits.liid.forumx.entity.CategoryEntity;

import java.util.UUID;

public interface CategoryUtilsService {
    boolean categoryExist(UUID id);
    boolean categoryHasChild(UUID id);
    CategoryEntity findById(UUID id);
}
