package com.hits.liid.forumx.repository;

import com.hits.liid.forumx.entity.MessageAttachmentEntity;
import com.hits.liid.forumx.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface MessageAttachmentRepository extends JpaRepository<MessageAttachmentEntity, UUID> {
    Set<MessageAttachmentEntity> findAllByParentMessage(MessageEntity message);
}
