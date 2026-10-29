package vladimir.java_ekzamen.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import vladimir.java_ekzamen.entity.Role;
import vladimir.java_ekzamen.entity.User;
import vladimir.java_ekzamen.repository.UserRepository;
import vladimir.java_ekzamen.security.UserDetailsImpl;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void accessDeniedForAnonymousUser() throws Exception {
        mockMvc.perform(get("/tickets"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void accessGrantedForAuthenticatedUser() throws Exception {
        User realUser = new User();
        realUser.setId(1L);
        realUser.setUsername("user");
        realUser.setPassword("encoded");
        realUser.setFullName("Test User");

        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setName("ROLE_USER");
        realUser.setRoles(Set.of(userRole));

        UserDetailsImpl userDetails = new UserDetailsImpl(realUser);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(realUser));

        mockMvc.perform(get("/tickets")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isOk());
    }
}