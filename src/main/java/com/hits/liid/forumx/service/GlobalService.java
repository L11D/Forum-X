package com.hits.liid.forumx.service;

import com.hits.liid.forumx.model.GlobalSearchResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface GlobalService {
    GlobalSearchResponse searchSubstring(@Valid @NotBlank String substring);
}
