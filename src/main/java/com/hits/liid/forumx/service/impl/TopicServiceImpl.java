package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.CategoryEntity;
import com.hits.liid.forumx.entity.MessageEntity;
import com.hits.liid.forumx.entity.TopicEntity;
import com.hits.liid.forumx.entity.UserEntity;
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
import com.hits.liid.forumx.service.UserService;
import com.hits.liid.forumx.utils.JwtTokenUtils;
import jakarta.transaction.Transactional;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UserService userService;
    @Override
    @Transactional
    public List<TopicDTO> searchSubstring(@Valid @NotBlank String substring) {
        List<TopicEntity> topicEntities = topicRepository.findBySubstring(substring);
        List<TopicDTO> topics = new ArrayList<>();
        for (TopicEntity t: topicEntities){
            topics.add(topicEntityToDto(t));
        }
        return topics;
    }

    @Override
    @Transactional
    public TopicsPaginationDTO getTopics(SortingValues sorting, @Valid @Min(1) int pageSize, @Valid @Min(0) int page) {
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

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<TopicEntity> topicsPage = topicRepository.findAll(pageable);
        List<TopicDTO> topics = new ArrayList<>();
        for (TopicEntity t : topicsPage.stream().toList()){
            topics.add(topicEntityToDto(t));
        }
        return new TopicsPaginationDTO(topics, new Pagination(
                topicsPage.getSize(),
                topicsPage.getTotalPages(),
                topicsPage.getNumber()
        ));
    }

    private TopicDTO topicEntityToDto(TopicEntity entity){
        return new TopicDTO(
                entity.getId(),
                entity.getName(),
                entity.getParentCategory() != null ? entity.getParentCategory().getId() : null,
                entity.getUserCreator() != null ? entity.getUserCreator().getId() : null,
                entity.getUserCreator() != null ? entity.getUserCreator().getNickname() : null,
                entity.getCreationDate(),
                entity.getEditingDate()
        );
    }

    @SneakyThrows
    @Transactional
    public UUID create(@Valid CreateTopicRequest request, Authentication authentication) {
        UUID userId = tokenUtils.getUserIdFromAuthentication(authentication);
        UserEntity user = userService.getUserById(userId);
        CategoryEntity parentCategory = categoryUtilsService.findById(request.categoryId());

        if (categoryUtilsService.categoryHasChild(request.categoryId())){
            throw new HasChildException("Topics can only be created in lower-level categories");
        }

        TopicEntity topic = TopicEntity.of(
                null,
                user,
                LocalDateTime.now(),
                null,
                request.name(),
                parentCategory);
        TopicEntity createdTopic = topicRepository.save(topic);
        return createdTopic.getId();
    }

    @SneakyThrows
    @Transactional
    @PreAuthorize("@topicServiceImpl.checkPermission(#topicId, authentication) || hasRole('ADMIN')")
    public void edit(@Valid EditTopicRequest request, UUID topicId) {
        TopicEntity topic = topicRepository.findById(topicId).orElseThrow(() ->
                new NotFoundException("Topic not found"));

        CategoryEntity parentCategory = categoryUtilsService.findById(request.categoryId());

        if (categoryUtilsService.categoryHasChild(request.categoryId())){
            throw new HasChildException("Topics can only be created in lower-level categories");
        }

        topic.setParentCategory(parentCategory);
        topic.setName(request.name());
        topic.setEditingDate(LocalDateTime.now());
        topicRepository.save(topic);
    }
    @SneakyThrows
    @Transactional
    @PreAuthorize("@topicServiceImpl.checkPermission(#topicId, authentication) || hasRole('ADMIN')")
    public void delete(UUID topicId) {
        TopicEntity topic = topicRepository.findById(topicId).orElseThrow(() ->
                new NotFoundException("Topic not found"));

        List<MessageEntity> messages = topicRepository.findMessageByTopicId(topicId);
        for (MessageEntity m : messages){
            messageService.delete(m.getId());
        }

        topicRepository.delete(topic);
    }

    @SneakyThrows
    @Transactional
    public boolean checkPermission(UUID topicId, Authentication authentication){
        UUID userId = tokenUtils.getUserIdFromAuthentication(authentication);

        TopicEntity topic = topicRepository.findById(topicId).orElseThrow(() ->
                new NotFoundException("Topic not found"));

        return userId.equals(topic.getUserCreator().getId());
    }

}
