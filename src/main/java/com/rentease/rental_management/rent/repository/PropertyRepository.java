package com.rentease.rental_management.rent.repository;

import com.rentease.rental_management.rent.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer>, JpaSpecificationExecutor<Property>
{
    Page<Property> findAllByUsers_Username(String username, Pageable pageable);

    Integer deleteByIdAndUsers_Username(Integer id, String username);

    @Query("SELECT p from Property AS p WHERE LOWER(p.address.locality) LIKE LOWER(CONCAT('%', :locality, '%')) OR " +
            "LOWER(p.address.subLocality) LIKE LOWER(CONCAT('%', :locality, '%'))")
    Page<Property> searchByLocalityOrSubLocality(String locality, Pageable pageable);

    Property findByIdAndUsers_Username(Integer id, String username);
}
