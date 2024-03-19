package com.hits.liid.forumx.repository;

import com.hits.liid.forumx.entity.MessageEntity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.TemporalType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID>, JpaSpecificationExecutor<MessageEntity> {

    @Query(value =
            "SELECT m FROM MessageEntity m WHERE m.topicId = :topicId")
    Page<MessageEntity> findByTopicId(@Param("topicId") UUID topicId, Pageable pageable);

    @Query(value =
            "SELECT m FROM MessageEntity m WHERE LOWER(m.text) LIKE CONCAT('%', LOWER(:substring), '%')")
    List<MessageEntity> findBySubstring(@Param("substring") String substring);

    @Query(value =
            "SELECT m FROM MessageEntity m " +
                    "JOIN TopicEntity t ON m.topicId = t.id " +
                    "JOIN CategoryEntity c ON c.id = t.categoryId " +
                    "WHERE (:substring IS NULL OR LOWER(m.text) LIKE CONCAT('%', LOWER(:substring), '%')) ")
    List<MessageEntity> search(
            @Param("substring") String substring
    );
}
