package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.MessageAttachmentEntity;
import com.hits.liid.forumx.entity.MessageEntity;
import com.hits.liid.forumx.entity.TopicEntity;
import com.hits.liid.forumx.entity.UserEntity;
import com.hits.liid.forumx.errors.HasChildException;
import com.hits.liid.forumx.errors.IncorrectDatesException;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.model.Pagination;
import com.hits.liid.forumx.model.SortingValues;
import com.hits.liid.forumx.model.message.*;
import com.hits.liid.forumx.model.topic.TopicDTO;
import com.hits.liid.forumx.repository.MessageAttachmentRepository;
import com.hits.liid.forumx.repository.MessageRepository;
import com.hits.liid.forumx.service.CategoryUtilsService;
import com.hits.liid.forumx.service.MessageService;
import com.hits.liid.forumx.service.TopicUtilsService;
import com.hits.liid.forumx.service.UserService;
import com.hits.liid.forumx.utils.JwtTokenUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final JwtTokenUtils tokenUtils;
    private final MessageRepository repository;
    private final TopicUtilsService topicUtilsService;
    private final CategoryUtilsService categoryUtilsService;
    private final MessageAttachmentRepository attachmentRepository;
    private final UserService userService;

    @SneakyThrows
    @Transactional
    public List<MessageDTO> search(SearchMessageRequest request) {

        if (request.creationDateStart() != null && request.creationDateEnd() != null){
            if (request.creationDateStart().isAfter(request.creationDateEnd())){
                throw new IncorrectDatesException("End date mast be after then start date");
            }
        }

        if (request.topicId() != null){
            if (!topicUtilsService.topicExist(request.topicId())){
                throw new NotFoundException("Topic not found");
            }
        }

        if (request.categoryId() != null){
            if (!categoryUtilsService.categoryExist(request.categoryId())){
                throw new NotFoundException("Category not found");
            }
        }

        if (request.userCreatorId() != null){
            if (!userService.userExist(request.userCreatorId())){
                throw new NotFoundException("User not found");
            }
        }

        List<MessageEntity> messagesEntity = repository.findAll(getSpecificationMessageSearch(request));
        return convertToDTO(messagesEntity);
    }

    private Specification<MessageEntity> getSpecificationMessageSearch(SearchMessageRequest request){
        var specPredicates = new ArrayList<Specification<MessageEntity>>();

        if (request.substring() != null && !request.substring().isEmpty()){
            specPredicates.add(((root, query, criteriaBuilder) -> {
                String likeExpression = "%" + request.substring().toLowerCase() + "%";
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("text")), likeExpression);
            }));
        }

        if (request.creationDateStart() != null){
            specPredicates.add(((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), request.creationDateStart())
            ));
        }

        if (request.creationDateEnd() != null){
            specPredicates.add(((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("creationDate"), request.creationDateEnd())
            ));
        }

        if (request.topicId() != null){
            specPredicates.add(((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("parentTopic").get("id"), request.topicId())
            ));
        }

        if (request.userCreatorId() != null){
            specPredicates.add(((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userCreator").get("id"), request.userCreatorId())
            ));
        }

        if (request.categoryId() != null){
            specPredicates.add(((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("parentTopic").get("parentCategory").get("id"), request.categoryId())
            ));
        }

        return Specification.allOf(specPredicates);
    }

    private List<MessageDTO> convertToDTO(List<MessageEntity> entities){
        List<MessageDTO> messages = new ArrayList<>();
        for (MessageEntity m : entities){
            Set<MessageAttachmentEntity> attachments = attachmentRepository.findAllByParentMessage(m);
            messages.add(new MessageDTO(
                    m.getId(),
                    m.getText(),
                    m.getParentTopic() != null ? m.getParentTopic().getId() : null,
                    m.getUserCreator() != null ? m.getUserCreator().getId() : null,
                    m.getUserCreator() != null ? m.getUserCreator().getNickname() : null,
                    m.getCreationDate(),
                    m.getEditingDate(),
                    attachments.stream()
                            .map(MessageAttachmentEntity::getId)
                            .collect(Collectors.toSet())
            ));
        }
        return messages;
    }

    @Transactional
    public List<MessageDTO> searchSubstring(@Valid @NotBlank String substring) {
        List<MessageEntity> messagesEntity = repository.findBySubstring(substring);
        List<MessageDTO> messages = convertToDTO(messagesEntity);
        return messages;
    }

    @Transactional
    public MessagePaginationDTO getMessages(UUID topicId, SortingValuesMessage sorting, @Valid @Min(1) int pageSize, @Valid @Min(0) int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        switch (sorting){
            case CREATION_DATE_ASC -> {
                sort = Sort.by(Sort.Direction.ASC, "creationDate");
            }
            case CREATION_DATE_DESC -> {
                sort = Sort.by(Sort.Direction.DESC, "creationDate");
            }
        }

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<MessageEntity> messagePage = repository.findByTopicId(topicId, pageable);

        List<MessageDTO> messages = convertToDTO(messagePage.stream().toList());

        return new MessagePaginationDTO(messages, new Pagination(
                messagePage.getSize(),
                messagePage.getTotalPages(),
                messagePage.getNumber()
        ));
    }

    @SneakyThrows
    @Transactional
    public UUID create(@Valid CreateMessageRequest request, Authentication authentication) {
        UUID userId = tokenUtils.getUserIdFromAuthentication(authentication);
        UserEntity user = userService.getUserById(userId);
        TopicEntity parentTopic = topicUtilsService.findTopicById(request.topicId());

        Set<MessageAttachmentEntity> attachmentEntities = new HashSet<>();

        for (UUID i : request.attachments()){
            attachmentEntities.add(attachmentRepository.findById(i).orElseThrow(() -> new NotFoundException("Attachment not found")));
        }

        MessageEntity message = MessageEntity.of(
                null,
                user,
                LocalDateTime.now(),
                null,
                request.text(),
                parentTopic);
        MessageEntity createdMessage = repository.save(message);


        for (MessageAttachmentEntity entity : attachmentEntities){
            entity.setParentMessage(createdMessage);
            log.info(entity.getFileName());
            attachmentRepository.save(entity);
        }

        return createdMessage.getId();
    }

    @SneakyThrows
    @Transactional
    @PreAuthorize("@messageServiceImpl.checkPermission(#messageId, authentication) || hasRole('ADMIN')")
    public void edit(@Valid EditMessageRequest request, UUID messageId) {
        MessageEntity message = repository.findById(messageId).orElseThrow(() ->
                new NotFoundException("Message not found"));

        message.setText(request.text());
        message.setEditingDate(LocalDateTime.now());
        repository.save(message);
    }

    @SneakyThrows
    @Transactional
    @PreAuthorize("@messageServiceImpl.checkPermission(#messageId, authentication) || hasRole('ADMIN')")
    public void delete(UUID messageId) {
        MessageEntity message = repository.findById(messageId).orElseThrow(() ->
                new NotFoundException("Message not found"));
        repository.delete(message);
    }

    @SneakyThrows
    @Transactional
    public boolean checkPermission(UUID messageId, Authentication authentication){
        UUID userId = tokenUtils.getUserIdFromAuthentication(authentication);

        MessageEntity message = repository.findById(messageId).orElseThrow(() ->
                new NotFoundException("Message not found"));

        return userId.equals(message.getUserCreator().getId());
    }
}
