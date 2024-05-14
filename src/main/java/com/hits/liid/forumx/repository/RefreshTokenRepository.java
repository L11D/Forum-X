package com.hits.liid.forumx.repository;

import com.hits.liid.forumx.entity.RefreshTokenEntity;
import com.hits.liid.forumx.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    void deleteAllByUser(UserEntity user);
}
