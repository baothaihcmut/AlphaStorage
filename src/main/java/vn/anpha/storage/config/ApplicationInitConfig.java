package vn.anpha.storage.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Role.Entity.Role;
import vn.anpha.storage.Role.respository.RoleRepository;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.respository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {

    @Bean
    ApplicationRunner applicationRunner(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            // Add some initial data to your database here.
            Role roleAdmin = new Role();
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleAdmin.setName("ADMIN");
                roleAdmin.setDescription("Admin role");
                roleRepository.save(roleAdmin);
            } else {
                roleAdmin = roleRepository.findByName("ADMIN").get(0);
            }
            if (roleRepository.findByName("USER").isEmpty()) {
                Role roleUser = new Role();
                roleUser.setName("USER");
                roleUser.setDescription("User role");
                roleRepository.save(roleUser);
            }
            if (userRepository.findByEmail("Admin@gmail.com").isEmpty()) {
                User user = new User();
                user.setEmail("admin@gmail.com");
                user.setPassword(new BCryptPasswordEncoder().encode("admin123"));
                user.setFullName("Admin");
                user.setRoleId(roleAdmin);

                userRepository.save(user);
            }
        };
    }
}
