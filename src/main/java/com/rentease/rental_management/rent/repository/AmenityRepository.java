package com.rentease.rental_management.rent.repository;

import com.rentease.rental_management.rent.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Integer>
{

}
