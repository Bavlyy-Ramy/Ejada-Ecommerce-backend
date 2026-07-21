package com.codewithbavly.ecommercebackend.config;

import com.codewithbavly.ecommercebackend.entity.Role;
import com.codewithbavly.ecommercebackend.entity.User;
import com.codewithbavly.ecommercebackend.entity.enums.RoleName;
import com.codewithbavly.ecommercebackend.repository.RoleRepository;
import com.codewithbavly.ecommercebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AppStarter implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) {

        if (roleRepository.findByName(RoleName.ADMIN).isEmpty()) {
            roleRepository.save(
                    Role.builder()
                            .name(RoleName.ADMIN)
                            .build()
            );
        }

        if (roleRepository.findByName(RoleName.USER).isEmpty()) {
            roleRepository.save(
                    Role.builder()
                            .name(RoleName.USER)
                            .build()
            );
        }
        if (userRepository.findByUsername("admin").isEmpty()) {

            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow();

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@ecommerce.com")
                    .firstName("System")
                    .lastName("Administrator")
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(admin);
        }
    }
}