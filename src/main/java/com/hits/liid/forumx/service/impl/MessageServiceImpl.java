package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.MessageEntity;
import com.hits.liid.forumx.entity.TopicEntity;
import com.hits.liid.forumx.errors.HasChildException;
import com.hits.liid.forumx.errors.IncorrectDatesException;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.model.Pagination;
import com.hits.liid.forumx.model.SortingValues;
import com.hits.liid.forumx.model.message.*;
import com.hits.liid.forumx.model.topic.TopicDTO;
import com.hits.liid.forumx.repository.MessageRepository;
import com.hits.liid.forumx.service.CategoryUtilsService;
import com.hits.liid.forumx.service.MessageService;
import com.hits.liid.forumx.service.TopicUtilsService;
import com.hits.liid.forumx.service.UserService;
import com.hits.liid.forumx.utils.JwtTokenUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final JwtTokenUtils tokenUtils;
    private final MessageRepository repository;
    private final TopicUtilsService topicUtilsService;
    private final CategoryUtilsService categoryUtilsService;
    private final UserService userService;

    @SneakyThrows
    @Override
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

//        List<MessageEntity> messagesEntity = repository.search(
//                request.substring()
//                //request.creationDateStart() == null ? null : Timestamp.valueOf(request.creationDateStart().toString())
//        );
//        List<MessageDTO> messages = convertToDTO(messagesEntity);

//        Specification<MessageEntity> spec = searchMessageBySubstring(request.substring(), UUID.fromString("9e40b5f3-39c2-408c-97a8-03b4ca64d671"));

        List<MessageEntity> messagesEntity = repository.findAll();
        List<MessageDTO> messages = convertToDTO(messagesEntity);
        return messages;
    }

    private Specification<MessageEntity> searchMessageBySubstring(String substring) {
        return (root, query, criteriaBuilder) -> {
            if (substring == null || substring.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                String likeExpression = "%" + substring.toLowerCase() + "%";
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("text")), likeExpression);
            }
        };
    }

//    private Specification<MessageEntity> searchMessageBySubstring(String substring, UUID topicId) {
//        return (root, query, criteriaBuilder) -> {
//            query.distinct(true); // Если необходимо исключить дубликаты
//
//            if (Objects.nonNull(topicId)) {
//                Join<MessageEntity, TopicEntity> topicJoin = root.join("topicId", JoinType.INNER);
//                query.where(criteriaBuilder.equal(topicJoin.get("id"), topicId));
//            }
//
//            if (substring != null && !substring.isEmpty()) {
//                String likeExpression = "%" + substring.toLowerCase() + "%";
//                query.where(criteriaBuilder.like(criteriaBuilder.lower(root.get("text")), likeExpression));
//            }
//
//            return null; // Для случая, если условия не заданы
//        };
//    }

    private List<MessageDTO> convertToDTO(List<MessageEntity> entities){
        List<MessageDTO> messages = new ArrayList<>();
        for (MessageEntity m : entities){
            messages.add(new MessageDTO(
                    m.getId(),
                    m.getText(),
                    m.getTopicId(),
                    m.getUserCreatorId(),
                    m.getCreationDate(),
                    m.getEditingDate()
            ));
        }
        return messages;
    }

    @Override
    public List<MessageDTO> searchSubstring(@Valid @NotBlank String substring) {
        List<MessageEntity> messagesEntity = repository.findBySubstring(substring);
        List<MessageDTO> messages = convertToDTO(messagesEntity);
        return messages;
    }

    @Override
    public MessagePaginationDTO getMessages(UUID topicId, SortingValuesMessage sorting, @Valid @Min(1) int pageSize, @Valid @Min(1) int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        switch (sorting){
            case CREATION_DATE_ASC -> {
                sort = Sort.by(Sort.Direction.ASC, "creationDate");
            }
            case CREATION_DATE_DESC -> {
                sort = Sort.by(Sort.Direction.DESC, "creationDate");
            }
        }

        Pageable pageable = PageRequest.of(page-1, pageSize, sort);
        Page<MessageEntity> messagePage = repository.findByTopicId(topicId, pageable);

        List<MessageDTO> messages = convertToDTO(messagePage.stream().toList());

        return new MessagePaginationDTO(messages, new Pagination(
                messagePage.getSize(),
                messagePage.getTotalPages(),
                messagePage.getNumber() + 1
        ));
    }

    @Override
    @SneakyThrows
    public UUID create(@Valid CreateMessageRequest request, Authentication authentication) {
        UUID userId = tokenUtils.getUserIdFromAuthentication(authentication);

        if (!topicUtilsService.topicExist(request.topicId())){
            throw new NotFoundException("Topic not found");
        }

        MessageEntity message = MessageEntity.of(
                null,
                userId,
                LocalDateTime.now(),
                null,
                request.text(),
                request.topicId());
        MessageEntity createdMessage = repository.save(message);
        return createdMessage.getId();
    }

    @Override
    @SneakyThrows
    public void edit(@Valid EditMessageRequest request, UUID messageId) {
        MessageEntity message = repository.findById(messageId).orElseThrow(() ->
                new NotFoundException("Message not found"));

        message.setText(request.text());
        message.setEditingDate(LocalDateTime.now());
        repository.save(message);
    }

    @Override
    @SneakyThrows
    public void delete(UUID messageId) {
        MessageEntity message = repository.findById(messageId).orElseThrow(() ->
                new NotFoundException("Message not found"));
        repository.delete(message);
    }
}
