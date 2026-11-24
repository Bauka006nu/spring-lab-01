package kz.iitu.springlab.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/css/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/books/**").hasAnyRole("USER", "ADMIN")
                // variant 5: a USER may reach the edit URL; the owner check is @PreAuthorize in BookService
                .requestMatchers(HttpMethod.POST, "/books/*/edit").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/books/**").hasRole("ADMIN")   // create, delete
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().denyAll())
            .formLogin(form -> form.permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/?logout"));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
