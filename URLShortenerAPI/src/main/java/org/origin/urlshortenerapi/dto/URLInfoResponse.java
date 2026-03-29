package org.origin.urlshortenerapi.dto;

import java.time.LocalDateTime;

public record URLInfoResponse(
        String shortURL,
        String fullURL,
        LocalDateTime createdDate
) {}
