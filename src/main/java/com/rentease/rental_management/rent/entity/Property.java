package com.rentease.rental_management.rent.entity;

import com.rentease.rental_management.auth.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    @Embedded
    private Address address;

    @Embedded
    private Price price;

    private Integer bedrooms;

    private Integer bathrooms;

    private Boolean isAttachedBathroom;

    private Integer balconies;

    private Boolean isAttachedBalcony;

    private Integer propertyFloor;

    private Integer totalFloor;

    private String facing;

    private Boolean isMainRoadFacing;

    private Integer buildUpArea;

    private Integer carpetArea;

    private Integer propertyAge;

    private LocalDate availableFrom;

    private Integer noticePeriodInMonths;

    private Boolean gatedSecurity;

    private Boolean gym;

    private Boolean onlyVeg;

    @Column(unique = true, nullable = false)
    private String uniqueHash;

    @ManyToMany
    @JoinTable(name = "propertyAmenities", joinColumns = @JoinColumn(name = "propertyId"),
            inverseJoinColumns = @JoinColumn(name = "amenityId"))
    private Set<Amenity> amenities;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "preferredTenants", joinColumns = @JoinColumn(name = "propertyId"))
    @Enumerated(EnumType.STRING)
    private Set<PreferredTenants> preferredTenants;

    public enum PreferredTenants
    {
        ANYONE, FAMILY, BACHELOR_MALE, BACHELOR_FEMALE, COMPANY
    }

    @Enumerated(EnumType.STRING)
    private PropertyCategory propertyCategory;

    public enum PropertyCategory
    {
        COMMERCIAL, RESIDENTIAL
    }

    @Enumerated(EnumType.STRING)
    private WaterSupply waterSupply;

    public enum WaterSupply
    {
        CORPORATION, BOREWELL, BOTH
    }

    @Enumerated(EnumType.STRING)
    private ListingCategory listingCategory;

    public enum ListingCategory
    {
        RENT_OR_LEASE, SALE, PG_OR_HOSTEL
    }

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    public enum PropertyType
    {
        FLAT_OR_APARTMENT, RESIDENTIAL_HOUSE, VILLA, STUDIO_APARTMENT, OFFICE_SPACE, SHOP, SHOWROOM, WAREHOUSE_OR_GODOWN, INDUSTRY, AGRICULTURAL
    }

    @Enumerated(EnumType.STRING)
    private FurnishedStatus furnishedStatus;

    public enum FurnishedStatus
    {
        FURNISHED, UNFURNISHED, SEMI_FURNISHED
    }

    @Enumerated(EnumType.STRING)
    private PossessionStatus possessionStatus;

    public enum PossessionStatus
    {
        UNDER_CONSTRUCTION, READY_TO_MOVE
    }

    @Enumerated(EnumType.STRING)
    private Parking parking;

    public enum Parking
    {
        CAR, BIKE, BOTH, NONE
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private Users users;

}
