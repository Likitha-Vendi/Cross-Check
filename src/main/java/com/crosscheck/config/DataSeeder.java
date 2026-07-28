package com.crosscheck.config;

import com.crosscheck.model.User;
import com.crosscheck.repository.UserRepository;
import com.crosscheck.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

    @Value("${crosscheck.admin.name:CrossCheck Administrator}")
    private String adminName;

    @Value("${crosscheck.admin.email:admin@crosscheck.local}")
    private String adminEmail;

    @Value("${crosscheck.admin.password:ChangeMe123}")
    private String adminPassword;

    @Bean
    CommandLineRunner seed(UserRepository users, AuthService auth) {
        return args -> {
            enforceSingleAdmin(users, auth);
        };
    }

    private void enforceSingleAdmin(UserRepository users, AuthService auth) {
        List<User> admins = users.findAllByRoleIgnoreCaseOrderByIdAsc("ADMIN");

        if (admins.isEmpty()) {
            User admin = new User();
            admin.setName(adminName);
            admin.setEmail(adminEmail.toLowerCase());
            admin.setPassword(auth.encode(adminPassword));
            admin.setRole("ADMIN");
            admin.setActive(true);

            users.save(admin);
        } else {
            User primary = admins.get(0);
            primary.setActive(true);
            users.save(primary);

            for (int i = 1; i < admins.size(); i++) {
                User extra = admins.get(i);
                extra.setActive(false);
                users.save(extra);
            }
        }
    }
}