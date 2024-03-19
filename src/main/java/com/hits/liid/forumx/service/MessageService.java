package com.hits.liid.forumx.service;

import com.hits.liid.forumx.model.SortingValues;
import com.hits.liid.forumx.model.message.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<MessageDTO> search(SearchMessageRequest request);
    List<MessageDTO> searchSubstring(@Valid @NotBlank String substring);
    MessagePaginationDTO getMessages(UUID topicId, SortingValuesMessage sorting, @Valid @Min(1) int pageSize, @Valid @Min(1) int page);
    UUID create(@Valid CreateMessageRequest request, Authentication authentication);
    void edit(@Valid EditMessageRequest request, UUID messageId);
    void delete(UUID messageId);
}
