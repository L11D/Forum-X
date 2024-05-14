package com.hits.liid.forumx.model.user;

import java.util.UUID;

public record JwtResponse(String accessToken, UUID refreshToken) {
}
