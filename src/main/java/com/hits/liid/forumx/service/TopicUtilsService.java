package com.hits.liid.forumx.service;

import com.hits.liid.forumx.entity.TopicEntity;

import java.util.UUID;

public interface TopicUtilsService {
    boolean topicExist(UUID id);
    TopicEntity findTopicById(UUID id);

}
