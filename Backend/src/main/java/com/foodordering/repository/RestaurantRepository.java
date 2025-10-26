package com.foodordering.repository;

import com.foodordering.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByCity(String city);
    List<Restaurant> findByIsOpen(Boolean isOpen);
    List<Restaurant> findByOwnerId(Long ownerId);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
