package com.hits.liid.forumx.model;

import com.hits.liid.forumx.model.category.CategorySearchDTO;
import com.hits.liid.forumx.model.message.MessageDTO;
import com.hits.liid.forumx.model.topic.TopicDTO;

import java.util.List;

public record GlobalSearchResponse(
        List<CategorySearchDTO> categories,
        List<TopicDTO> topics,
        List<MessageDTO> messages
) {
}
