package com.hits.liid.forumx.service.impl;

import com.hits.liid.forumx.entity.RefreshTokenEntity;
import com.hits.liid.forumx.entity.UserEntity;
import com.hits.liid.forumx.errors.NotFoundException;
import com.hits.liid.forumx.errors.TokenExpireException;
import com.hits.liid.forumx.repository.RefreshTokenRepository;
import com.hits.liid.forumx.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.jwt.refresh_lifetime}")
    private Duration tokenLifetime;

    @Transactional
    public UUID createToken(UserEntity user){
        refreshTokenRepository.deleteAllByUser(user);
        LocalDateTime expireDate = LocalDateTime.now().plusNanos(tokenLifetime.toNanos());
        RefreshTokenEntity token = RefreshTokenEntity.of(null, expireDate, user);
        refreshTokenRepository.save(token);
        return token.getId();
    }

    @SneakyThrows
//    @Transactional
    public UserEntity getUserFromToken(UUID tokenId){
        RefreshTokenEntity token = refreshTokenRepository.findById(tokenId).orElseThrow(
                () -> new NotFoundException("Refresh token not found"));
        if (LocalDateTime.now().isAfter(token.getExpireDate())){
            refreshTokenRepository.delete(token);
            throw new TokenExpireException("Refresh token is expired");
        }
        return token.getUser();
    }

}
