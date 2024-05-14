package com.hits.liid.forumx.model.category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CategoryDTO(
        UUID id,
        String name,
        UUID userCreatorId,
        String userNickname,
        LocalDateTime creationDate,
        LocalDateTime editingDate,
        List<CategoryDTO> childCategories
) {
}
