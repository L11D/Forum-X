package com.hits.liid.forumx.service;

import java.util.UUID;

public interface CategoryUtilsService {
    public boolean categoryExist(UUID id);
    public boolean categoryHasChild(UUID id);
}
