package com.hits.liid.forumx.repository;

import com.hits.liid.forumx.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByNickname(String nickname);

    @Query(
            value = "SELECT EXISTS " +
                    "(SELECT 1 FROM user_category uc " +
                    "WHERE uc.user_id = :userId " +
                    "LIMIT 1)",
            nativeQuery = true
    )
    boolean isModerator(@Param("userId") UUID userId);
}
