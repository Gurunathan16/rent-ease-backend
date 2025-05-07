package com.rentease.rental_management.rent.service;

import com.rentease.rental_management.rent.dto.PropertyInfo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PropertyLikeService {
    @Transactional
    ResponseEntity<Map<String, Object>> toggleLike(@Valid PropertyInfo propertyInfo);

    ResponseEntity<Map<String, Object>> getLikeCount(PropertyInfo propertyInfo);
}
