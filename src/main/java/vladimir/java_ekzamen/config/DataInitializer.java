package vladimir.java_ekzamen.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vladimir.java_ekzamen.entity.Role;
import vladimir.java_ekzamen.entity.User;
import vladimir.java_ekzamen.repository.RoleRepository;
import vladimir.java_ekzamen.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            Role managerRole = new Role();
            managerRole.setName("ROLE_MANAGER");
            roleRepository.save(managerRole);

            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            createUser("admin", "admin", "Admin User", Set.of(adminRole));
            createUser("manager", "manager", "Manager User", Set.of(managerRole));
            createUser("user", "user", "Regular User", Set.of(userRole));
        }
    }

    private void createUser(String username, String password, String fullName, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
    }
}