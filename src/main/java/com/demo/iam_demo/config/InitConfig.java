package com.demo.iam_demo.config;

import com.demo.iam_demo.model.Role;
import com.demo.iam_demo.model.User;
import com.demo.iam_demo.repository.RoleRepository;
import com.demo.iam_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class InitConfig {
    private static final String ADMIN_ROLE_NAME = "ADMIN";
    private static final String USER_ROLE_NAME = "USER";
    private static final String DEFAULT_PASSWORD = "123456";

    @Bean
    ApplicationRunner initData(
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            UserRepository userRepository
    ){
        return args -> {
            log.info("Initializing default roles and admin user.");

            //tạo role nếu chưa có
            Role userRole = roleRepository.findByName(USER_ROLE_NAME)
                    .orElseGet(() -> roleRepository.save(Role.builder().name(USER_ROLE_NAME).build()));
            Role adminRole = roleRepository.findByName(ADMIN_ROLE_NAME)
                    .orElseGet(() -> roleRepository.save(Role.builder().name(ADMIN_ROLE_NAME).build()));

            final String adminEmail = "admin123@gmail.com";
            final String adminUsername = "admin123";

            // tạo admin user nếu chưa có
            if(userRepository.findByEmail(adminEmail).isEmpty()){
                User admin = User.builder()
                        .email(adminEmail)
                        .username(adminUsername)
                        .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                        .firstName("System")
                        .lastName("Admin")
                        .build();

                // gán quyền admin
                admin.getRoles().add(adminRole);
                userRepository.save(admin);
                log.warn("Admin user created: {} / {}", adminEmail, DEFAULT_PASSWORD);
                log.info("Please change the password after first login");
            }
            log.info("Initialization complete. Total roles: {}", roleRepository.count());
        };
    }
}