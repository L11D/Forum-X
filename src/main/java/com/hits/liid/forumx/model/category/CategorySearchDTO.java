package com.hits.liid.forumx.model.category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategorySearchDTO(
        UUID id,
        String name,
        UUID parentCategoryId,
        UUID userCreatorId,
        String userNickName,
        LocalDateTime creationDate,
        LocalDateTime editingDate
) {
}
