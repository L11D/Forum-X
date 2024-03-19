package com.hits.liid.forumx.model.topic;

import java.time.LocalDateTime;
import java.util.UUID;

public record TopicDTO(
        UUID id,
        String name,
        UUID categoryId,
        UUID userCreatorId,
        LocalDateTime creationDate,
        LocalDateTime editingDate
) {
}
