package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.MessageEntity;
import com.hits.liid.forumx.entity.TopicEntity;
import com.hits.liid.forumx.errors.HasChildException;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.model.Pagination;
import com.hits.liid.forumx.model.SortingValues;
import com.hits.liid.forumx.model.topic.CreateTopicRequest;
import com.hits.liid.forumx.model.topic.EditTopicRequest;
import com.hits.liid.forumx.model.topic.TopicDTO;
import com.hits.liid.forumx.model.topic.TopicsPaginationDTO;
import com.hits.liid.forumx.repository.TopicRepository;
import com.hits.liid.forumx.service.CategoryUtilsService;
import com.hits.liid.forumx.service.MessageService;
import com.hits.liid.forumx.service.TopicService;
import com.hits.liid.forumx.utils.JwtTokenUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final CategoryUtilsService categoryUtilsService;
    private final MessageService messageService;
    private final JwtTokenUtils tokenUtils;

    @Override
    public List<TopicDTO> searchSubstring(@Valid @NotBlank String substring) {
        List<TopicEntity> topicEntities = topicRepository.findBySubstring(substring);
        List<TopicDTO> topics = new ArrayList<>();
        for (TopicEntity t: topicEntities){
            topics.add(new TopicDTO(
                    t.getId(),
                    t.getName(),
                    t.getCategoryId(),
                    t.getUserCreatorId(),
                    t.getCreationDate(),
                    t.getEditingDate()
            ));
        }
        return topics;
    }

    @Override
    public TopicsPaginationDTO getTopics(SortingValues sorting, @Valid @Min(1) int pageSize, @Valid @Min(1) int page) {
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

        Pageable pageable = PageRequest.of(page-1, pageSize, sort);
        Page<TopicEntity> topicsPage = topicRepository.findAll(pageable);
        List<TopicDTO> topics = new ArrayList<>();
        for (TopicEntity t : topicsPage.stream().toList()){
            topics.add(new TopicDTO(
                    t.getId(),
                    t.getName(),
                    t.getCategoryId(),
                    t.getUserCreatorId(),
                    t.getCreationDate(),
                    t.getEditingDate()
            ));
        }
        return new TopicsPaginationDTO(topics, new Pagination(
                topicsPage.getSize(),
                topicsPage.getTotalPages(),
                topicsPage.getNumber()+1
        ));
    }

    @SneakyThrows
    public UUID create(@Valid CreateTopicRequest request, Authentication authentication) {
        UUID userId = tokenUtils.getUserIdFromAuthentication(authentication);

        if (!categoryUtilsService.categoryExist(request.categoryId())){
            throw new NotFoundException("Category not found");
        }
        if (categoryUtilsService.categoryHasChild(request.categoryId())){
            throw new HasChildException("Topics can only be created in lower-level categories");
        }

        TopicEntity topic = TopicEntity.of(
                null,
                userId,
                LocalDateTime.now(),
                null,
                request.name(),
                request.categoryId());
        TopicEntity createdTopic = topicRepository.save(topic);
        return createdTopic.getId();
    }

    @SneakyThrows
    public void edit(@Valid EditTopicRequest request, UUID topicId) {
        TopicEntity topic = topicRepository.findById(topicId).orElseThrow(() ->
                new NotFoundException("Topic not found"));

        if (!categoryUtilsService.categoryExist(request.categoryId())){
            throw new NotFoundException("Category not found");
        }
        if (categoryUtilsService.categoryHasChild(request.categoryId())){
            throw new HasChildException("Topics can only be created in lower-level categories");
        }

        topic.setCategoryId(request.categoryId());
        topic.setName(request.name());
        topic.setEditingDate(LocalDateTime.now());
        topicRepository.save(topic);
    }

    @SneakyThrows
    public void delete(UUID topicId) {
        TopicEntity topic = topicRepository.findById(topicId).orElseThrow(() ->
                new NotFoundException("Topic not found"));

        List<MessageEntity> messages = topicRepository.findMessageByCategoryId(topicId);
        for (MessageEntity m : messages){
            messageService.delete(m.getId());
        }

        topicRepository.delete(topic);
    }
}
