package com.hits.liid.forumx.service;
import com.hits.liid.forumx.entity.TopicEntity;
import com.hits.liid.forumx.model.SortingValues;
import com.hits.liid.forumx.model.category.CategorySearchDTO;
import com.hits.liid.forumx.model.topic.CreateTopicRequest;
import com.hits.liid.forumx.model.topic.EditTopicRequest;
import com.hits.liid.forumx.model.topic.TopicDTO;
import com.hits.liid.forumx.model.topic.TopicsPaginationDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface TopicService {
    List<TopicDTO> searchSubstring(@Valid @NotBlank String substring);
    TopicsPaginationDTO getTopics(SortingValues sorting, @Valid @Min(1) int pageSize, @Valid @Min(0) int page);
    UUID create(@Valid CreateTopicRequest request, Authentication authentication);
    void edit(@Valid EditTopicRequest request, UUID topicId);
    void delete(UUID topicId);
}
