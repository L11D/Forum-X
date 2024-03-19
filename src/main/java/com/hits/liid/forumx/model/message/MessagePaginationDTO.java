package com.hits.liid.forumx.model.message;

import com.hits.liid.forumx.model.Pagination;

import java.util.List;

public record MessagePaginationDTO(
        List<MessageDTO> messages,
        Pagination pagination
) {
}
