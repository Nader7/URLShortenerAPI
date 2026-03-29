package org.origin.urlshortenerapi.repositories;

import org.origin.urlshortenerapi.entity.URLMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URLMappingsRepository extends JpaRepository<URLMappingEntity, String> { }