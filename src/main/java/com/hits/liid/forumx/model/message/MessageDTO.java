package com.hits.liid.forumx.model.message;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record MessageDTO(
        UUID id,
        String text,
        UUID topicId,
        UUID userCreatorId,
        String userNickname,
        LocalDateTime creationDate,
        LocalDateTime editingDate,
        Set<UUID> attachments
        ) {
}
