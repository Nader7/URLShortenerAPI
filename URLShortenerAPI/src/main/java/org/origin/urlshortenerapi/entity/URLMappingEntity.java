package org.origin.urlshortenerapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class URLMappingEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String shortURL;

    @Column(nullable = false, unique = false)
    private String originalURL;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    public URLMappingEntity(){}

    public URLMappingEntity(String shortURL, String originalURL){
        this.shortURL = shortURL;
        this.originalURL = originalURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof URLMappingEntity that)) return false;
        return Objects.equals(shortURL, that.shortURL) && Objects.equals(originalURL, that.originalURL) && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortURL, originalURL, createdDate);
    }
}
