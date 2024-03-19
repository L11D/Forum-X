package com.hits.liid.forumx.model.validation;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {
}
