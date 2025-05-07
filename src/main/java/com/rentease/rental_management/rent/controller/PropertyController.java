package com.rentease.rental_management.rent.controller;

import com.rentease.rental_management.rent.dto.PropertyInfo;
import com.rentease.rental_management.rent.dto.PropertyRegistration;
import com.rentease.rental_management.rent.dto.PropertyUpdate;
import com.rentease.rental_management.rent.service.impl.PropertyLikeServiceImpl;
import com.rentease.rental_management.rent.service.impl.PropertyServiceImpl;
import com.rentease.rental_management.util.response.ResponseEntityHandler;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/app")
public class PropertyController
{
    private final PropertyServiceImpl propertyService;
    private final PropertyLikeServiceImpl propertyLikeServiceImpl;

    public PropertyController(PropertyServiceImpl propertyService, PropertyLikeServiceImpl propertyLikeServiceImpl)
    {
        this.propertyService = propertyService;
        this.propertyLikeServiceImpl = propertyLikeServiceImpl;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody PropertyRegistration propertyRegistration
            , BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return propertyService.registerProperty(propertyRegistration);
    }

    @PostMapping("/edit")
    public ResponseEntity<Map<String, Object>> edit(@Valid @RequestBody PropertyUpdate propertyUpdate
            , BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return propertyService.editProperty(propertyUpdate);
    }

    @PostMapping("/view")
    public ResponseEntity<Map<String, Object>> property(@Valid @RequestBody PropertyInfo propertyInfo
            , BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return propertyService.viewProperty(propertyInfo);
    }

    @GetMapping("/myProperties")
    public ResponseEntity<Map<String, Object>> myProperties(Pageable pageable)
    {
        return propertyService.viewMyProperties(pageable);
    }

    @GetMapping("/properties")
    public ResponseEntity<Map<String, Object>> properties(Pageable pageable)
    {
        return propertyService.viewProperties(pageable);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> delete(@Valid @RequestBody PropertyInfo propertyInfo,
                                              BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return propertyService.delete(propertyInfo);
    }

    @GetMapping("/localitySearch/{locality}")
    public ResponseEntity<Map<String, Object>> localitySearch(@PathVariable String locality, Pageable pageable)
    {
        return propertyService.searchByLocality(locality, pageable);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filter(@RequestParam(required = false) Set<String> propertyType,
                                                      @RequestParam(required = false) Set<Integer> bhkType,
                                                      @RequestParam(required = false) Integer minPrice,
                                                      @RequestParam(required = false) Integer maxPrice,
                                                      @RequestParam(required = false) Integer availableWithin,
                                                      @RequestParam(required = false) Set<String> preferredTenants,
                                                      @RequestParam(required = false) Set<String> furnishedStatus,
                                                      @RequestParam(required = false) Set<String> parking,
                                                      @RequestParam(required = false) String listingCategory,
                                                      @RequestParam(required = false) Integer propertyAge,
                                                      @RequestParam(required = false) Integer bathrooms,
                                                      @RequestParam(required = false) Set<Integer> amenities,
                                                      @RequestParam(required = false) Integer minArea,
                                                      @RequestParam(required = false) Integer maxArea,
                                                      Pageable pageable)
    {
        return propertyService.applyFiler(propertyType, bhkType, minPrice, maxPrice, availableWithin, preferredTenants, furnishedStatus, parking, listingCategory, propertyAge, bathrooms, amenities, minArea, maxArea, pageable);
    }

    @PostMapping("/sellerDetails")
    public ResponseEntity<Map<String, Object>> sellerDetails(@Valid @RequestBody PropertyInfo propertyInfo,
                                                             BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return propertyService.getSellerDetails(propertyInfo);
    }

    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> like(@Valid @RequestBody PropertyInfo propertyInfo, BindingResult bindingResult)
    {
        if(bindingResult.hasFieldErrors())
            return validationErrorBuilder(bindingResult);

        return propertyLikeServiceImpl.toggleLike(propertyInfo);
    }

    @PostMapping("/likeCount")
    public ResponseEntity<Map<String, Object>> likeCount(@Valid @RequestBody PropertyInfo propertyInfo,
                                                     BindingResult bindingResult)
    {
        if(bindingResult.hasFieldErrors())
            return validationErrorBuilder(bindingResult);

        return propertyLikeServiceImpl.getLikeCount(propertyInfo);
    }

    private ResponseEntity<Map<String, Object>> validationErrorBuilder(BindingResult bindingResult)
    {
        List<String> errors = new ArrayList<>();

        errors.addAll(bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList());

        errors.addAll(bindingResult.getGlobalErrors().stream().map(ObjectError::getDefaultMessage).toList());

        return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Validation check failed.", "Validation Errors", errors);
    }

}
