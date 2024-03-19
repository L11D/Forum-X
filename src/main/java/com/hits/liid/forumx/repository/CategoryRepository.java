package com.hits.liid.forumx.repository;

import com.hits.liid.forumx.entity.CategoryEntity;
import com.hits.liid.forumx.entity.MessageEntity;
import com.hits.liid.forumx.entity.TopicEntity;
import com.hits.liid.forumx.entity.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    @Query(value = "SELECT c FROM CategoryEntity c WHERE c.parentCategoryId = :parentId")
    List<CategoryEntity> findChildCategories(@Param("parentId") UUID parentId, Sort sort);
    default List<CategoryEntity> findChildCategories(UUID parentId) {
        return findChildCategories(parentId, Sort.unsorted());
    }


    @Query(value =
            "SELECT EXISTS " +
            "(SELECT 1 FROM category c " +
                    "WHERE c.parent_category_id = :parentId " +
                    "LIMIT 1)",
            nativeQuery = true)
    boolean hasChildCategories(@Param("parentId") UUID parentId);

    @Query(value =
            "SELECT EXISTS " +
                    "(SELECT 1 FROM category c " +
                    "JOIN topic t ON t.category_id = c.id AND c.id = :id " +
                    "LIMIT 1)",
            nativeQuery = true)
    boolean hasTopics(@Param("id") UUID id);

    @Query(value =
            "SELECT t FROM TopicEntity t " +
            "JOIN CategoryEntity c ON t.categoryId = c.id AND c.id = :id ")
    List<TopicEntity> findTopicsByCategoryId(@Param("id") UUID id);

    @Query(value =
            "SELECT c FROM CategoryEntity c WHERE c.parentCategoryId IS NULL")
    List<CategoryEntity> findTopCategories(Sort sort);

    @Query(value =
            "SELECT c FROM CategoryEntity c WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:substring), '%')")
    List<CategoryEntity> findBySubstring(@Param("substring") String substring);

}
