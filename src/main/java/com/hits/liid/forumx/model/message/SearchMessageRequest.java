package com.hits.liid.forumx.model.message;

import java.time.LocalDateTime;
import java.util.UUID;

public record SearchMessageRequest(
        String substring,
        LocalDateTime creationDateStart,
        LocalDateTime creationDateEnd,
        UUID userCreatorId,
        UUID topicId,
        UUID categoryId
) {
}
