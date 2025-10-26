package com.foodordering.service;

import com.foodordering.dto.RestaurantDto;
import com.foodordering.entity.Restaurant;
import com.foodordering.entity.User;
import com.foodordering.exception.ResourceNotFoundException;
import com.foodordering.repository.RestaurantRepository;
import com.foodordering.repository.UserRepository;
import com.foodordering.mapper.RestaurantMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Transactional
    public RestaurantDto createRestaurant(RestaurantDto dto) {
        // Resolve owner from authenticated user
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        // Use mapper to create entity from DTO, then set relations
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        restaurant.setOwner(owner);
        restaurant.setIsOpen(true);
        restaurant.setApprovalStatus(com.foodordering.enums.ApprovalStatus.PENDING); // New restaurants need approval
        
        // Set default values for rating and reviews
        if (restaurant.getRating() == null) {
            restaurant.setRating(0.0);
        }
        if (restaurant.getTotalReviews() == null) {
            restaurant.setTotalReviews(0);
        }
        
        if (restaurant.getCreatedAt() == null) {
            restaurant.setCreatedAt(LocalDateTime.now());
        }

        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    public List<RestaurantDto> getAllRestaurants() {
    // Only return APPROVED restaurants to regular users
    return restaurantRepository.findAll().stream()
        .filter(r -> r.getApprovalStatus() == com.foodordering.enums.ApprovalStatus.APPROVED)
        .map(restaurantMapper::toDto)
        .collect(Collectors.toList());
    }

    public RestaurantDto getRestaurantById(Long id) {
    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
    return restaurantMapper.toDto(restaurant);
    }

    public List<RestaurantDto> getRestaurantsByCity(String city) {
    return restaurantRepository.findByCity(city).stream()
        .map(restaurantMapper::toDto)
        .collect(Collectors.toList());
    }

    public List<RestaurantDto> getRestaurantsForOwner() {
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        return restaurantRepository.findByOwnerId(owner.getId()).stream()
                .map(restaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RestaurantDto updateRestaurant(Long id, RestaurantDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        // Ensure the authenticated user is the owner
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        if (restaurant.getOwner() == null || !email.equals(restaurant.getOwner().getEmail())) {
            throw new org.springframework.security.access.AccessDeniedException("Not authorized to update this restaurant");
        }

        restaurant.setName(dto.getName());
        restaurant.setDescription(dto.getDescription());
        restaurant.setImageUrl(dto.getImageUrl());
        restaurant.setAddress(dto.getAddress());
        restaurant.setCity(dto.getCity());
        restaurant.setPhoneNumber(dto.getPhoneNumber());
        restaurant.setIsOpen(dto.getIsOpen());
        restaurant.setUpdatedAt(LocalDateTime.now());

        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        // Ensure the authenticated user is the owner
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        if (restaurant.getOwner() == null || !email.equals(restaurant.getOwner().getEmail())) {
            throw new org.springframework.security.access.AccessDeniedException("Not authorized to delete this restaurant");
        }

        restaurantRepository.deleteById(id);
    }

    // Mapping handled by RestaurantMapper
}
