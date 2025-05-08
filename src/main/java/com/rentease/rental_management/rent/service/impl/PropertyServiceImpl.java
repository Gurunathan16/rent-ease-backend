package com.rentease.rental_management.rent.service.impl;

import com.rentease.rental_management.auth.dto.UserAsParty;
import com.rentease.rental_management.auth.entity.Users;
import com.rentease.rental_management.auth.repository.UsersRepository;
import com.rentease.rental_management.rent.dto.*;
import com.rentease.rental_management.rent.entity.Amenity;
import com.rentease.rental_management.rent.entity.Property;
import com.rentease.rental_management.rent.repository.AmenityRepository;
import com.rentease.rental_management.rent.repository.PropertyRepository;
import com.rentease.rental_management.rent.service.PropertyService;
import com.rentease.rental_management.util.hash.PropertyHash;
import com.rentease.rental_management.util.mail.EmailNotifier;
import com.rentease.rental_management.util.mappers.PropertyMappers;
import com.rentease.rental_management.util.mappers.UsersMapper;
import com.rentease.rental_management.util.response.ResponseEntityHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final AmenityRepository amenityRepository;
    private final PropertyMappers propertyMappers;
    private final UsersRepository usersRepository;
    private final EmailNotifier emailNotifier;

    public PropertyServiceImpl(PropertyRepository propertyRepository, AmenityRepository amenityRepository, PropertyMappers propertyMappers, UsersRepository usersRepository, EntityManager entityManager, EmailNotifier emailNotifier)
    {
        this.propertyRepository = propertyRepository;
        this.amenityRepository = amenityRepository;
        this.propertyMappers = propertyMappers;
        this.usersRepository = usersRepository;
        this.emailNotifier = emailNotifier;
    }

    @Override
    public ResponseEntity<Map<String, Object>> registerProperty(PropertyRegistration propertyRegistration)
    {
        Set<Amenity> propertyAmenities = new HashSet<>();
        Set<Integer> amenitiesId = propertyRegistration.getAmenities();

        if (amenitiesId != null)
        {
            for(Integer amenityId : amenitiesId)
            {
                Amenity amenity = amenityRepository.findById(amenityId).orElse(null);

                if(amenity == null)
                    return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Amenity not found.",
                            "Recovery", "Try providing the correct amenity Id.");

                propertyAmenities.add(amenity);
            }
        }

        PropertyHashDTO propertyHash = new PropertyHashDTO(propertyRegistration.getAddress().getFullAddress(),
                propertyRegistration.getAddress().getSubLocality(),propertyRegistration.getAddress().getLocality(),
                propertyRegistration.getAddress().getCity(), propertyRegistration.getAddress().getState(),
                propertyRegistration.getAddress().getPinCode(), propertyRegistration.getListingCategory().toString(), propertyRegistration.getPropertyType().toString(), propertyRegistration.getPropertyCategory().toString(),
                propertyRegistration.getPossessionStatus().toString());

        String uniqueHash = PropertyHash.uniquePropertyHash(propertyHash);

        Property property = propertyMappers.propertyRegistrationToProperty(propertyRegistration, propertyAmenities,
                propertyMappers.addressDtoToAddress(propertyRegistration.getAddress()),
                propertyMappers.priceDtoToPrice(propertyRegistration.getPrice()));

        property.setUsers(getUsers());
        property.setUniqueHash(uniqueHash);

        propertyRepository.save(property);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.CREATED, "Property created successfully.", "Details", property.getTitle());
    }

    @Override
    public ResponseEntity<Map<String, Object>> editProperty(PropertyUpdate propertyUpdate)
    {
        Property property = propertyRepository.findByIdAndUsers_Username(propertyUpdate.getId(), getUsername());

        if(property == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Property with that Id doesn't exists.", "Recovery", "Confirm the property Id.");

        Set<Amenity> propertyAmenities = new HashSet<>();
        Set<Integer> amenitiesId = propertyUpdate.getAmenities();

        if (amenitiesId != null)
        {
            for(Integer amenityId : amenitiesId)
            {
                Amenity amenity = amenityRepository.findById(amenityId).orElse(null);

                if(amenity == null)
                    return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Amenity not found.",
                            "Recovery", "Try providing the correct amenity Id.");

                propertyAmenities.add(amenity);
            }
        }

        Property propertyUpdated = propertyMappers.propertyUpdateToProperty(propertyUpdate, property,
                propertyAmenities, propertyMappers.addressDtoToAddress(propertyUpdate.getAddress()),
                propertyMappers.priceDtoToPrice(propertyUpdate.getPrice()));

        PropertyHashDTO propertyHash = new PropertyHashDTO(propertyUpdate.getAddress().getFullAddress(),
                propertyUpdate.getAddress().getSubLocality(),propertyUpdate.getAddress().getLocality(),
                propertyUpdate.getAddress().getCity(), propertyUpdate.getAddress().getState(),
                propertyUpdate.getAddress().getPinCode(), propertyUpdate.getListingCategory().toString(),
                propertyUpdate.getPropertyType().toString(), propertyUpdate.getPropertyCategory().toString(),
                propertyUpdate.getPossessionStatus());

        String uniqueHash = PropertyHash.uniquePropertyHash(propertyHash);

        propertyUpdated.setUniqueHash(uniqueHash);

        propertyRepository.save(propertyUpdated);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Property updated successfully.", "Details",
                property.getTitle());

    }

    @Override
    public ResponseEntity<Map<String, Object>> viewProperty(PropertyInfo propertyInfo)
    {
        Property property = propertyRepository.findById(propertyInfo.id()).orElse(null);

        if(property == null)
                return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Property with that Id doesn't exists.", "Recovery", "Confirm the property Id.");

        Set<Amenity> propertyAmenities = property.getAmenities();
        Set<String> amenitiesName = propertyAmenities.stream().map(Amenity::getName).collect(Collectors.toSet());

        PropertyProjection propertyProjection = propertyMappers.propertyToPropertyProjection(property, amenitiesName);

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Property fetched successfully.", "Details", propertyProjection);
    }

    @Override
    public ResponseEntity<Map<String, Object>> viewMyProperties(Pageable pageable)
    {
        Page<Property> properties = propertyRepository.findAllByUsers_Username(getUsername(), pageable);

        if(properties == null || properties.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "You haven't listed any property.", "Recovery", "Add your first property for free by clicking '+' button.");

        Page<PropertyProjection> propertiesProjection =
                properties.map(property ->
                        propertyMappers.propertyToPropertyProjection(property,
                                property.getAmenities().stream().map(Amenity::getName).collect(Collectors.toSet())));

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Your Properties fetched successfully.",
                "Details", propertiesProjection);
    }

    @Override
    public ResponseEntity<Map<String, Object>> viewProperties(Pageable pageable)
    {
        Page<Property> properties = propertyRepository.findAll(pageable);

        if(properties == null || properties.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "No properties available.",
                    "Recovery", "Please try again after some time.");

        Page<PropertyProjection> propertiesProjection =
                properties.map(property ->
                        propertyMappers.propertyToPropertyProjection(property,
                                property.getAmenities().stream().map(Amenity::getName).collect(Collectors.toSet())));

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Properties fetched successfully.", "Details", propertiesProjection);
    }

    @Transactional
    @Override
    public ResponseEntity<Map<String, Object>> delete(PropertyInfo propertyInfo)
    {
        Integer res = propertyRepository.deleteByIdAndUsers_Username(propertyInfo.id(), getUsername());

        if(res != null && res > 0)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Property deleted successfully.",
                    "Home", "/app/properties");

        return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "Property not found.",
            "Home", "/app/properties");
    }

    @Override
    public ResponseEntity<Map<String, Object>> searchByLocality(String locality, Pageable pageable)
    {
        Page<Property> propertiesByLocality = propertyRepository.searchByLocalityOrSubLocality(locality, pageable);

        if(propertiesByLocality == null || propertiesByLocality.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND,
                    "No properties available in " + locality + " locality.", "Recovery", "Please try by expanding " +
                            "your search a bit.");

        Page<PropertyProjection> propertiesProjection = propertiesByLocality.map(property ->
                propertyMappers.propertyToPropertyProjection(property,
                        property.getAmenities().stream().map(Amenity::getName).collect(Collectors.toSet())));

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK,
                "Properties at " + locality + " fetched successfully.", "Details", propertiesProjection);
    }

    @Override
    public ResponseEntity<Map<String, Object>> applyFiler(Set<String> propertyType, Set<Integer> bhkType,
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
                                                          Integer maxArea, Pageable pageable)
    {

        Specification<Property> specification = (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (propertyType != null && !propertyType.isEmpty())
                predicates.add(root.get("propertyType").in(propertyType));

            if (bhkType != null && !bhkType.isEmpty())
                predicates.add(root.get("bedrooms").in(bhkType));

            if (minPrice != null || maxPrice != null)
            {
                Integer min = minPrice == null ? 0 : minPrice;
                Integer max = maxPrice == null ? 1000000000 : maxPrice;

                predicates.add(criteriaBuilder.between(root.get("price").get("expectedPrice"),
                        min, max));
            }

            if (availableWithin != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo((root.get("availableFrom")),
                        LocalDate.now().plusDays(availableWithin)));

            if (preferredTenants != null && !preferredTenants.isEmpty())
            {
                Join<Property, Property.PreferredTenants> tenantsJoin = root.join("preferredTenants");
                CriteriaBuilder.In<Property.PreferredTenants> inClause = criteriaBuilder.in(tenantsJoin);

                for(String type : preferredTenants)
                {
                    try {
                        inClause.value(Property.PreferredTenants.valueOf(type));
                    }
                    catch(Exception ex)
                    {
                        throw new IllegalArgumentException("Wrong argument in preferred tenant filter.");
                    }
                }
                predicates.add(inClause);
            }

            if (furnishedStatus != null && !furnishedStatus.isEmpty())
                predicates.add(root.get("furnishedStatus").in(furnishedStatus));

            if (parking != null && !parking.isEmpty())
                predicates.add(root.get("parking").in(parking));

            if (listingCategory != null)
                predicates.add(criteriaBuilder.equal(root.get("listingCategory"), listingCategory));

            if (propertyAge != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("propertyAge"), propertyAge));

            if (bathrooms != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("bathrooms"), bathrooms));

            if (amenities != null && !amenities.isEmpty())
            {
                Join<Property, Amenity> amenityJoin = root.join("amenities");
                CriteriaBuilder.In<Integer> inClause = criteriaBuilder.in(amenityJoin.get("id"));

                for(Integer amenityId : amenities)
                {
                    try {
                        inClause.value(amenityId);
                    }
                    catch(Exception ex)
                    {
                        throw new IllegalArgumentException("Wrong argument in preferred tenant filter.");
                    }
                }
                predicates.add(inClause);
            }

            if (minArea != null || maxArea != null)
            {
                Integer min = minArea == null ? 0 : minArea;
                Integer max = maxArea == null ? 50000 : maxArea;

                predicates.add(criteriaBuilder.between(root.get("buildUpArea"), min, max));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Property> properties = propertyRepository.findAll(specification, pageable);

        if(properties == null || properties.isEmpty())
            return ResponseEntityHandler.getResponseEntity(HttpStatus.NOT_FOUND, "No properties available with that " +
                    "filters.", "Recovery", "Please try by adjusting your filter a bit.");

        List<PropertyProjection> propertyProjectionsList = new ArrayList<>();

        for(Property property : properties)
        {
            Set<Amenity> propertyAmenities = property.getAmenities();
            Set<String> amenitiesName = new HashSet<>();

            for(Amenity amenity : propertyAmenities)
            {
                amenitiesName.add(amenity.getName());
            }

            propertyProjectionsList.add(propertyMappers.propertyToPropertyProjection(property, amenitiesName));
        }

        Page<PropertyProjection> propertyProjections = new PageImpl<>(propertyProjectionsList,
                properties.getPageable(), properties.getTotalElements());

        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Filter applied successfully.", "Details",
                propertyProjections);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getSellerDetails(@Valid PropertyInfo propertyInfo)
    {
        Property property = propertyRepository.findById(propertyInfo.id()).orElse(null);

        if(property == null)
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Property with that Id doesn't exists.", "Recovery", "Confirm the property Id.");

        Users buyer = getUsers();
        UserAsParty buyerUserAsParty = UsersMapper.UsersToUserAsSeller(buyer);

        Users seller = property.getUsers();
        UserAsParty sellerUserAsParty = UsersMapper.UsersToUserAsSeller(seller);

        if(buyer.getEmail().equals(seller.getEmail()))
            return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Buyer and Seller cant be the same" +
                    " person.", "Recovery", "Try entering different property id.");

        Set<String> amenitiesName = property.getAmenities().stream().map(Amenity::getName).collect(Collectors.toSet());

        PropertyProjection propertyProjection = propertyMappers.propertyToPropertyProjection(property,
                amenitiesName);

        emailNotifier.notificationForBuyer(buyerUserAsParty.getEmail(), buyerUserAsParty.getFirstName() + " " + buyerUserAsParty.getLastName(), sellerUserAsParty, propertyProjection);

        emailNotifier.notificationForSeller(sellerUserAsParty.getEmail(), sellerUserAsParty.getFirstName() + " " + buyerUserAsParty.getLastName(), buyerUserAsParty, propertyProjection);


        return ResponseEntityHandler.getResponseEntity(HttpStatus.OK, "Property Seller details fetched successfully.", "Details", sellerUserAsParty);
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
