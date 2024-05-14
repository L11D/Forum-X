package com.hits.liid.forumx.service;

import com.hits.liid.forumx.entity.UserEntity;

import java.util.UUID;

public interface RefreshTokenService {
    UUID createToken(UserEntity user);
    UserEntity getUserFromToken(UUID tokenId);
}
