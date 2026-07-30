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

    @Value("${crosscheck.admin.name:RohitKumar}")
    private String adminName;

    @Value("${crosscheck.admin.email:HR@hourlyrecruit.com}")
    private String adminEmail;

    @Value("${crosscheck.admin.password:Bharath@1936}")
    private String adminPassword;

    @Bean
    public CommandLineRunner seed(UserRepository users, AuthService auth) {
        return args -> enforceSingleAdmin(users, auth);
    }

    private void enforceSingleAdmin(UserRepository users, AuthService auth) {

        List<User> admins = users.findAllByRoleIgnoreCaseOrderByIdAsc("ADMIN");

        if (admins.isEmpty()) {

            User admin = new User();
            admin.setName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(auth.encode(adminPassword));
            admin.setRole("ADMIN");
            admin.setActive(true);

            users.save(admin);

            System.out.println("=======================================");
            System.out.println("Admin user created successfully.");
            System.out.println("Name     : " + adminName);
            System.out.println("Email    : " + adminEmail);
            System.out.println("Password : " + adminPassword);
            System.out.println("=======================================");

        } else {

            User primary = admins.get(0);

            primary.setName(adminName);
            primary.setEmail(adminEmail);
            primary.setPassword(auth.encode(adminPassword));
            primary.setRole("ADMIN");
            primary.setActive(true);

            users.save(primary);

            // Disable duplicate admin accounts
            for (int i = 1; i < admins.size(); i++) {
                User extra = admins.get(i);
                extra.setActive(false);
                users.save(extra);
            }

            System.out.println("=======================================");
            System.out.println("Primary admin updated successfully.");
            System.out.println("Name     : " + adminName);
            System.out.println("Email    : " + adminEmail);
            System.out.println("Password : " + adminPassword);
            System.out.println("=======================================");
        }
    }
}
