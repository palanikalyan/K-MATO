package com.foodordering.config;

import com.foodordering.entity.User;
import com.foodordering.enums.Role;
import com.foodordering.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Data Initializer - Creates default admin user on application startup
 * This ensures there's always an admin account available for system management
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin already exists
            if (!userRepository.existsByEmail("admin@kmato.com")) {
                User admin = new User();
                admin.setEmail("admin@kmato.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setPhoneNumber("9999999999");
                admin.setRole(Role.ADMIN);
                admin.setIsActive(true);
                
                userRepository.save(admin);
                logger.info("✅ Default admin user created successfully!");
                logger.info("📧 Email: admin@kmato.com");
                logger.info("🔑 Password: Admin@123");
                logger.info("⚠️  Please change the password after first login!");
            } else {
                logger.info("ℹ️  Admin user already exists - skipping creation");
            }

            // Optional: Create sample restaurant owner for testing
            if (!userRepository.existsByEmail("owner@kmato.com")) {
                User owner = new User();
                owner.setEmail("owner@kmato.com");
                owner.setPassword(passwordEncoder.encode("Owner@123"));
                owner.setFirstName("Restaurant");
                owner.setLastName("Owner");
                owner.setPhoneNumber("8888888888");
                owner.setRole(Role.RESTAURANT_OWNER);
                owner.setIsActive(true);
                
                userRepository.save(owner);
                logger.info("✅ Sample restaurant owner created!");
                logger.info("📧 Email: owner@kmato.com");
                logger.info("🔑 Password: Owner@123");
            }

            // Optional: Create sample customer for testing
            if (!userRepository.existsByEmail("customer@kmato.com")) {
                User customer = new User();
                customer.setEmail("customer@kmato.com");
                customer.setPassword(passwordEncoder.encode("Customer@123"));
                customer.setFirstName("John");
                customer.setLastName("Doe");
                customer.setPhoneNumber("7777777777");
                customer.setRole(Role.CUSTOMER);
                customer.setIsActive(true);
                
                userRepository.save(customer);
                logger.info("✅ Sample customer created!");
                logger.info("📧 Email: customer@kmato.com");
                logger.info("🔑 Password: Customer@123");
            }

            logger.info("========================================");
            logger.info("🚀 Application ready with default users!");
            logger.info("========================================");
        };
    }
}
