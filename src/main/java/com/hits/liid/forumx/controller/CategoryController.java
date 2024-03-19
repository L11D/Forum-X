package com.hits.liid.forumx.controller;

import com.hits.liid.forumx.model.EntityCreatedResponse;
import com.hits.liid.forumx.model.SortingValues;
import com.hits.liid.forumx.model.category.*;
import com.hits.liid.forumx.model.message.MessageDTO;
import com.hits.liid.forumx.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("searchSubstring")
    @ResponseBody
    public List<CategorySearchDTO> searchSubstring(@RequestParam(value = "substring", required = true) String substring){
        return categoryService.searchSubstring(substring);
    }

    @GetMapping
    @ResponseBody
    public List<CategoryDTO> getCategories(@RequestParam(value = "sorting", defaultValue = "NAME_ASC") SortingValues sorting){

        return categoryService.getCategories(sorting);
    }

    @PostMapping("create")
    @ResponseBody
    public EntityCreatedResponse create(@RequestBody CreateCategoryRequest request, Authentication authentication){
        return new EntityCreatedResponse(categoryService.create(request, authentication));
    }

    @PutMapping("{id}/edit")
    @ResponseBody
    public void edit(@PathVariable UUID id,
                     @RequestBody EditCategoryRequest request){
        categoryService.edit(request, id);
    }

    @DeleteMapping("{id}/delete")
    @ResponseBody
    public void delete(@PathVariable UUID id, @RequestParam(value = "chainDelete", defaultValue = "false") boolean chainDelete){
        categoryService.delete(id, chainDelete);
    }
}
