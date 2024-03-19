package com.hits.liid.forumx.controller;

import com.hits.liid.forumx.entity.TopicEntity;
import com.hits.liid.forumx.model.EntityCreatedResponse;
import com.hits.liid.forumx.model.SortingValues;
import com.hits.liid.forumx.model.category.CategorySearchDTO;
import com.hits.liid.forumx.model.topic.CreateTopicRequest;
import com.hits.liid.forumx.model.topic.EditTopicRequest;
import com.hits.liid.forumx.model.topic.TopicDTO;
import com.hits.liid.forumx.model.topic.TopicsPaginationDTO;
import com.hits.liid.forumx.service.TopicService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/topic")
public class TopicController {
    private final TopicService topicService;

    @GetMapping("searchSubstring")
    @ResponseBody
    public List<TopicDTO> searchSubstring(@RequestParam(value = "substring", required = true) String substring){
        return topicService.searchSubstring(substring);
    }
    @GetMapping
    @ResponseBody
    public TopicsPaginationDTO getTopics(@RequestParam(value = "sorting", defaultValue = "CREATION_DATE_DESC") SortingValues sorting,
                                         @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                         @RequestParam(value = "page", defaultValue = "1") int page){

        return topicService.getTopics(sorting, pageSize, page);
    }

    @PostMapping("create")
    @ResponseBody
    public EntityCreatedResponse create(@RequestBody CreateTopicRequest request, Authentication authentication){
        return new EntityCreatedResponse(topicService.create(request, authentication));
    }

    @PutMapping("{id}/edit")
    @ResponseBody
    public void edit(@PathVariable UUID id,
                     @RequestBody EditTopicRequest request){
        topicService.edit(request, id);
    }

    @DeleteMapping("{id}/delete")
    @ResponseBody
    public void delete(@PathVariable UUID id){
        topicService.delete(id);
    }
}
