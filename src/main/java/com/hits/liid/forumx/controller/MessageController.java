package com.hits.liid.forumx.controller;

import com.hits.liid.forumx.model.EntityCreatedResponse;
import com.hits.liid.forumx.model.message.*;
import com.hits.liid.forumx.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("search")
    @ResponseBody
    public List<MessageDTO> search(@RequestBody SearchMessageRequest request){
        return messageService.search(request);
    }

    @GetMapping("searchSubstring")
    @ResponseBody
    public List<MessageDTO> searchSubstring(@RequestParam(value = "substring", required = true) String substring){
        return messageService.searchSubstring(substring);
    }

    @GetMapping("get/from/topic/{topicId}")
    @ResponseBody
    public MessagePaginationDTO getMessages(
            @PathVariable("topicId") UUID topicId,
            @RequestParam(value = "sorting", defaultValue = "CREATION_DATE_DESC") SortingValuesMessage sorting,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int page){

        return messageService.getMessages(topicId, sorting, pageSize, page);
    }

    @PostMapping("create")
    @ResponseBody
    public EntityCreatedResponse create(@RequestBody CreateMessageRequest request, Authentication authentication){
        return new EntityCreatedResponse(messageService.create(request, authentication));
    }

    @PutMapping("{id}/edit")
    @ResponseBody
    public void edit(@PathVariable UUID id,
                     @RequestBody EditMessageRequest request){
        messageService.edit(request, id);
    }

    @DeleteMapping("{id}/delete")
    @ResponseBody
    public void delete(@PathVariable UUID id){
        messageService.delete(id);
    }
}
