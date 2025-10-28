package com.foodordering.config;

import com.foodordering.entity.MenuItem;
import com.foodordering.entity.Restaurant;
import com.foodordering.entity.User;
import com.foodordering.enums.ApprovalStatus;
import com.foodordering.enums.Category;
import com.foodordering.enums.Role;
import com.foodordering.repository.MenuItemRepository;
import com.foodordering.repository.RestaurantRepository;
import com.foodordering.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * Data Initializer - Creates default users, restaurants, and menu items on application startup
 * This ensures there's always sample data available for testing and demonstration
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, 
                                   RestaurantRepository restaurantRepository,
                                   MenuItemRepository menuItemRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // ========================================
            // STEP 1: Create Default Users
            // ========================================
            
            User admin = null;
            User owner = null;
            User customer = null;
            
            // Check if admin already exists
            if (!userRepository.existsByEmail("admin@kmato.com")) {
                admin = new User();
                admin.setEmail("admin@kmato.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setFullName("Admin User");
                admin.setPhoneNumber("9999999999");
                admin.setRole(Role.ADMIN);
                admin.setIsActive(true);
                admin.setCreatedAt(LocalDateTime.now());
                
                admin = userRepository.save(admin);
                logger.info("✅ Default admin user created successfully!");
                logger.info("📧 Email: admin@kmato.com");
                logger.info("🔑 Password: Admin@123");
            } else {
                admin = userRepository.findByEmail("admin@kmato.com").orElse(null);
                logger.info("ℹ️  Admin user already exists - skipping creation");
            }

            // Optional: Create sample restaurant owner for testing
            if (!userRepository.existsByEmail("owner@kmato.com")) {
                owner = new User();
                owner.setEmail("owner@kmato.com");
                owner.setPassword(passwordEncoder.encode("Owner@123"));
                owner.setFullName("Restaurant Owner");
                owner.setPhoneNumber("8888888888");
                owner.setRole(Role.RESTAURANT_OWNER);
                owner.setIsActive(true);
                owner.setCreatedAt(LocalDateTime.now());
                
                owner = userRepository.save(owner);
                logger.info("✅ Sample restaurant owner created!");
                logger.info("📧 Email: owner@kmato.com");
                logger.info("🔑 Password: Owner@123");
            } else {
                owner = userRepository.findByEmail("owner@kmato.com").orElse(null);
                logger.info("ℹ️  Restaurant owner already exists - skipping creation");
            }

            // Optional: Create sample customer for testing
            if (!userRepository.existsByEmail("customer@kmato.com")) {
                customer = new User();
                customer.setEmail("customer@kmato.com");
                customer.setPassword(passwordEncoder.encode("Customer@123"));
                customer.setFullName("John Doe");
                customer.setPhoneNumber("7777777777");
                customer.setRole(Role.CUSTOMER);
                customer.setIsActive(true);
                customer.setCreatedAt(LocalDateTime.now());
                
                customer = userRepository.save(customer);
                logger.info("✅ Sample customer created!");
                logger.info("📧 Email: customer@kmato.com");
                logger.info("🔑 Password: Customer@123");
            } else {
                customer = userRepository.findByEmail("customer@kmato.com").orElse(null);
                logger.info("ℹ️  Customer already exists - skipping creation");
            }

            // ========================================
            // STEP 2: Create Sample Restaurants
            // ========================================
            
            if (owner != null && restaurantRepository.count() == 0) {
                logger.info("========================================");
                logger.info("🍽️  Creating sample restaurants...");
                logger.info("========================================");
                
                // Restaurant 1: Pizza Palace
                Restaurant pizzaPalace = Restaurant.builder()
                        .name("Pizza Palace")
                        .description("Authentic Italian pizzas with fresh ingredients")
                        .imageUrl("https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500")
                        .address("123 Main St")
                        .city("Mumbai")
                        .phoneNumber("9988776655")
                        .rating(4.5)
                        .totalReviews(120)
                        .isOpen(true)
                        .approvalStatus(ApprovalStatus.APPROVED)
                        .owner(owner)
                        .createdAt(LocalDateTime.now())
                        .build();
                pizzaPalace = restaurantRepository.save(pizzaPalace);
                logger.info("✅ Created: Pizza Palace");
                
                // Restaurant 2: Burger Hub
                Restaurant burgerHub = Restaurant.builder()
                        .name("Burger Hub")
                        .description("Juicy burgers and crispy fries")
                        .imageUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500")
                        .address("456 Park Ave")
                        .city("Mumbai")
                        .phoneNumber("8877665544")
                        .rating(4.2)
                        .totalReviews(85)
                        .isOpen(true)
                        .approvalStatus(ApprovalStatus.APPROVED)
                        .owner(owner)
                        .createdAt(LocalDateTime.now())
                        .build();
                burgerHub = restaurantRepository.save(burgerHub);
                logger.info("✅ Created: Burger Hub");
                
                // Restaurant 3: Sushi Express
                Restaurant sushiExpress = Restaurant.builder()
                        .name("Sushi Express")
                        .description("Fresh sushi and Japanese cuisine")
                        .imageUrl("https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=500")
                        .address("789 Ocean Blvd")
                        .city("Mumbai")
                        .phoneNumber("7766554433")
                        .rating(4.8)
                        .totalReviews(200)
                        .isOpen(true)
                        .approvalStatus(ApprovalStatus.APPROVED)
                        .owner(owner)
                        .createdAt(LocalDateTime.now())
                        .build();
                sushiExpress = restaurantRepository.save(sushiExpress);
                logger.info("✅ Created: Sushi Express");
                
                // ========================================
                // STEP 3: Create Menu Items
                // ========================================
                
                if (menuItemRepository.count() == 0) {
                    logger.info("========================================");
                    logger.info("🍕 Creating menu items for Pizza Palace...");
                    logger.info("========================================");
                    
                    // Pizza Palace Menu Items
                    MenuItem margherita = MenuItem.builder()
                            .name("Margherita Pizza")
                            .description("Classic tomato sauce, mozzarella, and basil")
                            .price(299.0)
                            .imageUrl("https://images.unsplash.com/photo-1604068549290-dea0e4a305ca?w=500")
                            .category(Category.MAIN_COURSE)
                            .isAvailable(true)
                            .isVegetarian(true)
                            .restaurant(pizzaPalace)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(margherita);
                    logger.info("  ✅ Margherita Pizza - ₹299");
                    
                    MenuItem pepperoni = MenuItem.builder()
                            .name("Pepperoni Pizza")
                            .description("Tomato sauce, mozzarella, and pepperoni")
                            .price(399.0)
                            .imageUrl("https://images.unsplash.com/photo-1628840042765-356cda07504e?w=500")
                            .category(Category.MAIN_COURSE)
                            .isAvailable(true)
                            .isVegetarian(false)
                            .restaurant(pizzaPalace)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(pepperoni);
                    logger.info("  ✅ Pepperoni Pizza - ₹399");
                    
                    MenuItem veggieSupreme = MenuItem.builder()
                            .name("Veggie Supreme")
                            .description("Loaded with fresh vegetables")
                            .price(349.0)
                            .imageUrl("https://images.unsplash.com/photo-1571997478779-2adcbbe9ab2f?w=500")
                            .category(Category.MAIN_COURSE)
                            .isAvailable(true)
                            .isVegetarian(true)
                            .restaurant(pizzaPalace)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(veggieSupreme);
                    logger.info("  ✅ Veggie Supreme - ₹349");
                    
                    MenuItem garlicBread = MenuItem.builder()
                            .name("Garlic Bread")
                            .description("Crispy bread with garlic butter")
                            .price(99.0)
                            .imageUrl("https://images.unsplash.com/photo-1573140401552-388e7e2f0e8d?w=500")
                            .category(Category.SNACKS)
                            .isAvailable(true)
                            .isVegetarian(true)
                            .restaurant(pizzaPalace)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(garlicBread);
                    logger.info("  ✅ Garlic Bread - ₹99");
                    
                    logger.info("========================================");
                    logger.info("🍔 Creating menu items for Burger Hub...");
                    logger.info("========================================");
                    
                    // Burger Hub Menu Items
                    MenuItem classicBurger = MenuItem.builder()
                            .name("Classic Burger")
                            .description("Beef patty with lettuce, tomato, and sauce")
                            .price(199.0)
                            .imageUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500")
                            .category(Category.MAIN_COURSE)
                            .isAvailable(true)
                            .isVegetarian(false)
                            .restaurant(burgerHub)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(classicBurger);
                    logger.info("  ✅ Classic Burger - ₹199");
                    
                    MenuItem cheeseBurger = MenuItem.builder()
                            .name("Cheese Burger")
                            .description("Double cheese with beef patty")
                            .price(249.0)
                            .imageUrl("https://images.unsplash.com/photo-1572802419224-296b0aeee0d9?w=500")
                            .category(Category.MAIN_COURSE)
                            .isAvailable(true)
                            .isVegetarian(false)
                            .restaurant(burgerHub)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(cheeseBurger);
                    logger.info("  ✅ Cheese Burger - ₹249");
                    
                    MenuItem veggieBurger = MenuItem.builder()
                            .name("Veggie Burger")
                            .description("Plant-based patty with fresh veggies")
                            .price(179.0)
                            .imageUrl("https://images.unsplash.com/photo-1520072959219-c595dc870360?w=500")
                            .category(Category.MAIN_COURSE)
                            .isAvailable(true)
                            .isVegetarian(true)
                            .restaurant(burgerHub)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(veggieBurger);
                    logger.info("  ✅ Veggie Burger - ₹179");
                    
                    MenuItem frenchFries = MenuItem.builder()
                            .name("French Fries")
                            .description("Crispy golden fries")
                            .price(79.0)
                            .imageUrl("https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=500")
                            .category(Category.SNACKS)
                            .isAvailable(true)
                            .isVegetarian(true)
                            .restaurant(burgerHub)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(frenchFries);
                    logger.info("  ✅ French Fries - ₹79");
                    
                    logger.info("========================================");
                    logger.info("🍣 Creating menu items for Sushi Express...");
                    logger.info("========================================");
                    
                    // Sushi Express Menu Items
                    MenuItem californiaRoll = MenuItem.builder()
                            .name("California Roll")
                            .description("Crab, avocado, and cucumber")
                            .price(349.0)
                            .imageUrl("https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=500")
                            .category(Category.MAIN_COURSE)
                            .isAvailable(true)
                            .isVegetarian(false)
                            .restaurant(sushiExpress)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(californiaRoll);
                    logger.info("  ✅ California Roll - ₹349");
                    
                    MenuItem salmonNigiri = MenuItem.builder()
                            .name("Salmon Nigiri")
                            .description("Fresh salmon over rice")
                            .price(299.0)
                            .imageUrl("https://images.unsplash.com/photo-1564489563601-c53cfc451e93?w=500")
                            .category(Category.MAIN_COURSE)
                            .isAvailable(true)
                            .isVegetarian(false)
                            .restaurant(sushiExpress)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(salmonNigiri);
                    logger.info("  ✅ Salmon Nigiri - ₹299");
                    
                    MenuItem veggieTempura = MenuItem.builder()
                            .name("Vegetable Tempura")
                            .description("Crispy fried vegetables")
                            .price(249.0)
                            .imageUrl("https://images.unsplash.com/photo-1611143669185-af224c5e3252?w=500")
                            .category(Category.APPETIZER)
                            .isAvailable(true)
                            .isVegetarian(true)
                            .restaurant(sushiExpress)
                            .createdAt(LocalDateTime.now())
                            .build();
                    menuItemRepository.save(veggieTempura);
                    logger.info("  ✅ Vegetable Tempura - ₹249");
                    
                    logger.info("========================================");
                    logger.info("✅ Successfully created 11 menu items!");
                    logger.info("========================================");
                }
            } else if (restaurantRepository.count() > 0) {
                logger.info("ℹ️  Restaurants already exist - skipping creation");
            } else {
                logger.warn("⚠️  Could not create restaurants - owner user not found");
            }

            logger.info("========================================");
            logger.info("🚀 Application ready with sample data!");
            logger.info("========================================");
        };
    }
}
