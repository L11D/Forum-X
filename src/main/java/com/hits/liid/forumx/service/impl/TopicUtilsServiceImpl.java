package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.repository.TopicRepository;
import com.hits.liid.forumx.service.TopicUtilsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class TopicUtilsServiceImpl implements TopicUtilsService {
    private final TopicRepository repository;

    @Override
    public boolean topicExist(UUID id) {
        return repository.findById(id).isPresent();
    }
}
