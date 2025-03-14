package edu.eci.arep.TallerSecureApp.config;

import edu.eci.arep.TallerSecureApp.model.User;
import edu.eci.arep.TallerSecureApp.repository.jpa.JpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Configura CORS
                .csrf(AbstractHttpConfigurer::disable)  // Deshabilita CSRF
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); //Cualquier solicitud está permitida sin autenticación
//                .authorizeHttpRequests(auth -> auth
//                        .dispatcherTypeMatchers(HttpMethod.valueOf("/public/**")).permitAll()  // Rutas públicas
//                        .anyRequest().authenticated());  // Todas las demás rutas requieren autenticación
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://localhost:8443"));  // Permite solicitudes desde este origen
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // Métodos permitidos
        configuration.setAllowedHeaders(List.of("*"));  // Todos los encabezados permitidos
        configuration.setAllowCredentials(true);  // Permite credenciales (cookies, encabezados de autenticación)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Aplica la configuración a todas las rutas
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService(JpaUserRepository userRepository) {
        return email -> {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.emptyList()
            );
        };
    }
}