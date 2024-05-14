package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.model.GlobalSearchResponse;
import com.hits.liid.forumx.service.CategoryService;
import com.hits.liid.forumx.service.GlobalService;
import com.hits.liid.forumx.service.MessageService;
import com.hits.liid.forumx.service.TopicService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class GlobalServiceImpl implements GlobalService {
    private final CategoryService categoryService;
    private final TopicService topicService;
    private  final MessageService messageService;

    @Override
    @Transactional
    public GlobalSearchResponse searchSubstring(@Valid @NotBlank String substring) {
        return new GlobalSearchResponse(
                categoryService.searchSubstring(substring),
                topicService.searchSubstring(substring),
                messageService.searchSubstring(substring)
        );
    }
}
