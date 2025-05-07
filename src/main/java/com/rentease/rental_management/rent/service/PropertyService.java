package com.rentease.rental_management.rent.service;

import com.rentease.rental_management.rent.dto.PropertyInfo;
import com.rentease.rental_management.rent.dto.PropertyRegistration;
import com.rentease.rental_management.rent.dto.PropertyUpdate;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;

public interface PropertyService {
    ResponseEntity<Map<String, Object>> registerProperty(PropertyRegistration propertyRegistration);

    ResponseEntity<Map<String, Object>> editProperty(PropertyUpdate propertyUpdate);

    ResponseEntity<Map<String, Object>> viewProperty(PropertyInfo propertyInfo);

    ResponseEntity<Map<String, Object>> viewMyProperties(Pageable pageable);

    ResponseEntity<Map<String, Object>> viewProperties(Pageable pageable);

    @Transactional
    ResponseEntity<Map<String, Object>> delete(PropertyInfo propertyInfo);

    ResponseEntity<Map<String, Object>> searchByLocality(String locality, Pageable pageable);

    ResponseEntity<Map<String, Object>> applyFiler(Set<String> propertyType, Set<Integer> bhkType,
                                                   Integer minPrice,
                                                   Integer maxPrice,
                                                   Integer availableWithin,
                                                   Set<String> preferredTenants,
                                                   Set<String> furnishedStatus,
                                                   Set<String> parking,
                                                   String listingCategory,
                                                   Integer propertyAge,
                                                   Integer bathrooms,
                                                   Set<Integer> amenities,
                                                   Integer minArea,
                                                   Integer maxArea, Pageable pageable);

    ResponseEntity<Map<String, Object>> getSellerDetails(@Valid PropertyInfo propertyInfo);
}
