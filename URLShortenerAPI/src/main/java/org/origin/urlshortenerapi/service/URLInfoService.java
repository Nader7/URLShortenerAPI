package org.origin.urlshortenerapi.service;

import org.origin.urlshortenerapi.dto.URLInfoResponse;
import org.origin.urlshortenerapi.entity.URLMappingEntity;
import org.origin.urlshortenerapi.repositories.URLMappingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class URLInfoService {

    @Value("${domain.prefix}")
    private String domainPrefix;

    @Autowired
    private URLMappingsRepository urlMappingsRepository;

    public URLInfoResponse findURLInfo(String shortURL){
        if(shortURL.length() != 6) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL is not valid"); }

        URLMappingEntity urlEntry = urlMappingsRepository.findById(shortURL)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found"));

        return new URLInfoResponse(domainPrefix + urlEntry.getShortURL(), urlEntry.getOriginalURL(), urlEntry.getCreatedDate());
    }
}
