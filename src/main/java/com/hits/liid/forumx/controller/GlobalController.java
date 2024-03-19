package com.hits.liid.forumx.controller;

import com.hits.liid.forumx.model.GlobalSearchResponse;
import com.hits.liid.forumx.service.GlobalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class GlobalController {

    private final GlobalService globalService;

    @GetMapping("searchSubstring")
    @ResponseBody
    public GlobalSearchResponse searchSubstring(@RequestParam(value = "substring", required = true) String substring){
        return globalService.searchSubstring(substring);
    }

}
