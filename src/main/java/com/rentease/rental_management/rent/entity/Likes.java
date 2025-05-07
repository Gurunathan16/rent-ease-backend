package com.rentease.rental_management.rent.entity;

import com.rentease.rental_management.auth.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Likes
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "propertyId", nullable = false)
    private Property property;

    @CreationTimestamp
    private LocalDateTime likedAt;
}
