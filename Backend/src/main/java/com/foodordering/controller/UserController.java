package com.foodordering.controller;

import com.foodordering.dto.UserDto;
import com.foodordering.entity.User;
import com.foodordering.mapper.RestaurantMapper;
import com.foodordering.mapper.UserMapper;
import com.foodordering.repository.RestaurantRepository;
import com.foodordering.repository.UserRepository;
import com.foodordering.security.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public UserController(UserRepository userRepository, UserMapper userMapper, RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        String email = SecurityUtils.getCurrentUserEmail();
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserDto dto = userMapper.toDto(user);

        // If the user is an owner/admin, include restaurants they own
        try {
            var restaurants = restaurantRepository.findByOwnerId(user.getId());
            if (restaurants != null) {
                dto.setRestaurants(restaurants.stream().map(restaurantMapper::toDto).collect(Collectors.toList()));
            }
        } catch (Exception ignored) {
            // Don't fail user endpoint if restaurant lookup fails for some reason.
        }

        return ResponseEntity.ok(dto);
    }
}
