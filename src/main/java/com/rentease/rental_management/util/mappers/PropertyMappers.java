package com.rentease.rental_management.util.mappers;

import com.rentease.rental_management.rent.dto.*;
import com.rentease.rental_management.rent.entity.Address;
import com.rentease.rental_management.rent.entity.Amenity;
import com.rentease.rental_management.rent.entity.Price;
import com.rentease.rental_management.rent.entity.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PropertyMappers
{
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "uniqueHash", ignore = true)
    @Mapping(target = "amenities", source = "propertyAmenities")
    @Mapping(target = "address", source = "addressDtoToEntity")
    @Mapping(target = "price", source = "priceDtoToEntity")
    Property propertyRegistrationToProperty(PropertyRegistration propertyRegistration, Set<Amenity> propertyAmenities,
                                            Address addressDtoToEntity, Price priceDtoToEntity);

    Address addressDtoToAddress(AddressDTO addressDTO);

    Price priceDtoToPrice(PriceDTO priceDTO);

    @Mapping(target = "amenities", source = "amenitiesName")
    PropertyProjection propertyToPropertyProjection(Property property, Set<String> amenitiesName);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "uniqueHash", ignore = true)
    @Mapping(target = "amenities", source = "propertyAmenities")
    @Mapping(target = "address", source = "addressDtoToEntity")
    @Mapping(target = "price", source = "priceDtoToEntity")
    void propertyUpdateToProperty(PropertyUpdate propertyUpdate, @MappingTarget  Property property,
                                      Set<Amenity> propertyAmenities, Address addressDtoToEntity,
                                      Price priceDtoToEntity);
}
