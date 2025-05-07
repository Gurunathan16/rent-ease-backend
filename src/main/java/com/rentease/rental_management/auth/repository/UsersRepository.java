package com.rentease.rental_management.auth.repository;

import com.rentease.rental_management.auth.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>
{

    Users findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(U) > 0 THEN TRUE ELSE FALSE END FROM Users AS U WHERE LOWER(username) = LOWER(:username)")
    Boolean existsByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(U) > 0 THEN TRUE ELSE FALSE END FROM Users AS U WHERE LOWER(email) = LOWER(:email)")
    Boolean existsByEmail(String email);


    void deleteByUsername(String username);

    Users findByEmail(String email);

    Users findByPhoneNumber(String integer);

    boolean existsByPhoneNumber(String phoneNumber);
}
