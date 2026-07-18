package com.price_tracker.security;

import com.price_tracker.domain.entities.user_entities.Role;
import com.price_tracker.domain.entities.user_entities.UserEntity;
import com.price_tracker.repositories.user_repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Seeds the initial admin account on startup so there is always at least one way to authenticate. */
@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    @Autowired
    public AdminUserInitializer(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 @Value("${app.admin.username}") String adminUsername,
                                 @Value("${app.admin.password}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(adminUsername).isPresent()) {
            return;
        }

        userRepository.save(UserEntity.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .build());
    }
}