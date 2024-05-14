package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.TopicEntity;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.repository.TopicRepository;
import com.hits.liid.forumx.service.TopicUtilsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class TopicUtilsServiceImpl implements TopicUtilsService {
    private final TopicRepository repository;

    @Override
    @Transactional
    public boolean topicExist(UUID id) {
        return repository.findById(id).isPresent();
    }

    @SneakyThrows
    @Override
    @Transactional
    public TopicEntity findTopicById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Topic not found"));
    }
}
