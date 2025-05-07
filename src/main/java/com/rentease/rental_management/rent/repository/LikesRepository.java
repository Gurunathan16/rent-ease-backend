package com.rentease.rental_management.rent.repository;

import com.rentease.rental_management.auth.entity.Users;
import com.rentease.rental_management.rent.entity.Likes;
import com.rentease.rental_management.rent.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Integer>
{

    Boolean existsByUsersAndProperty(Users users, Property property);

    void deleteByUsersAndProperty(Users users, Property property);

    Integer countByProperty(Property property);
}
