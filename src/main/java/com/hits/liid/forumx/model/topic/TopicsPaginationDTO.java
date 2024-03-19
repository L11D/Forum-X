package com.hits.liid.forumx.model.topic;

import com.hits.liid.forumx.model.Pagination;

import java.util.List;

public record TopicsPaginationDTO(
        List<TopicDTO> topics,
        Pagination pagination
) {
}
