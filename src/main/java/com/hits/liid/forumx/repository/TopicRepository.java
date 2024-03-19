package com.hits.liid.forumx.repository;

import com.hits.liid.forumx.entity.CategoryEntity;
import com.hits.liid.forumx.entity.MessageEntity;
import com.hits.liid.forumx.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TopicRepository extends JpaRepository<TopicEntity, UUID> {
    @Query(value =
            "SELECT m FROM MessageEntity m " +
                    "JOIN TopicEntity t ON m.topicId = t.id AND t.id = :id ")
    List<MessageEntity> findMessageByCategoryId(@Param("id") UUID id);

    @Query(value =
            "SELECT t FROM TopicEntity t WHERE LOWER(t.name) LIKE CONCAT('%', LOWER(:substring), '%')")
    List<TopicEntity> findBySubstring(@Param("substring") String substring);
}
