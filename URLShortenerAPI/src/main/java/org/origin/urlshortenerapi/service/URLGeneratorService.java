package org.origin.urlshortenerapi.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.origin.urlshortenerapi.dto.ShortURLResponse;
import org.origin.urlshortenerapi.entity.URLMappingEntity;
import org.origin.urlshortenerapi.repositories.URLMappingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class URLGeneratorService {

    @Value("${domain.prefix}")
    private String domainPrefix;

    @Autowired
    private URLMappingsRepository urlMappingsRepository;

    @Retryable(
            retryFor = { DataIntegrityViolationException.class },
            maxAttempts = 4
    )
    public ShortURLResponse constructShortURL(String url) {
        if(!isValidURL(url)) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL is not valid"); }

        URLMappingEntity urlMappingEntity = saveShortURL(url);
        return new ShortURLResponse(domainPrefix + urlMappingEntity.getShortURL());
    }

    private URLMappingEntity saveShortURL(String url){
        URLMappingEntity urlMappingEntity = new URLMappingEntity(generateShortURL(), url);
        return urlMappingsRepository.save(urlMappingEntity);
    }

    private String generateShortURL(){
        //in memory H2 database is not case sensitive
        return RandomStringUtils.secure().nextAlphanumeric(6).toLowerCase();
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
        return true;
    }

    @Recover
    private ShortURLResponse recover(DataIntegrityViolationException e, String url) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Short URL generation failed. Please try again.");
    }
}
