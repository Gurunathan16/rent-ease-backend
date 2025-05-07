package com.rentease.rental_management.rent.service.impl;

import com.rentease.rental_management.auth.entity.Users;
import com.rentease.rental_management.auth.repository.UsersRepository;
import com.rentease.rental_management.rent.dto.PropertyInfo;
import com.rentease.rental_management.rent.entity.Likes;
import com.rentease.rental_management.rent.entity.Property;
import com.rentease.rental_management.rent.repository.LikesRepository;
import com.rentease.rental_management.rent.repository.PropertyRepository;
import com.rentease.rental_management.rent.service.PropertyLikeService;
import com.rentease.rental_management.util.response.ResponseEntityHandler;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PropertyLikeServiceImpl implements PropertyLikeService {
    private final LikesRepository likesRepository;
    private final UsersRepository usersRepository;
    private final PropertyRepository propertyRepository;

    public PropertyLikeServiceImpl(LikesRepository likesRepository, UsersRepository usersRepository, PropertyRepository propertyRepository)
    {
        this.likesRepository = likesRepository;
        this.usersRepository = usersRepository;
        this.propertyRepository = propertyRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<Map<String, Object>> toggleLike(@Valid PropertyInfo propertyInfo)
    {
        Property property = propertyRepository.findById(propertyInfo.id()).orElse(null);

        if(property == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Property with that Id doesn't exists.", "Recovery", "Confirm the property Id.");

        Boolean result = likesRepository.existsByUsersAndProperty(getUsers(), property);

        if(result)
        {
            likesRepository.deleteByUsersAndProperty(getUsers(), property);

            return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Property unliked successfully.", "Details", property.getTitle() + " unliked.");
        }
        else
        {
            Likes likes = new Likes();
            likes.setProperty(property);
            likes.setUsers(getUsers());

            likesRepository.save(likes);

            return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Property liked successfully.", "Details", property.getTitle() + " liked.");
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getLikeCount(PropertyInfo propertyInfo)
    {
        Property property = propertyRepository.findById(propertyInfo.id()).orElse(null);

        if(property == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Property with that Id doesn't exists.", "Recovery", "Confirm the property Id.");

        Integer likeCount = likesRepository.countByProperty(property);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Property like count fetched successfully.",
                "Likes Count", likeCount);
    }

    private Users getUsers()
    {
        final String username = getUsername();

        return usersRepository.findByUsername(username);
    }

    private String getUsername()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

}
