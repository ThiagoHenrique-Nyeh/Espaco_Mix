
package com.example.espacomix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/cliente").permitAll()

                        .requestMatchers("/cliente/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/produto", "/produto/**").permitAll()

                        .requestMatchers("/produto", "/produto/**").hasRole("ADMIN")

                        .requestMatchers("/estoque/**").hasRole("ADMIN")

                        .requestMatchers("/administrador/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/pedido").hasRole("CLIENTE")

                        .requestMatchers(HttpMethod.GET, "/pedido").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/pedido/cliente/**")
                        .hasAnyRole("ADMIN", "CLIENTE")

                        .requestMatchers(HttpMethod.GET, "/pedido/*")
                        .hasAnyRole("ADMIN", "CLIENTE")

                        .requestMatchers(HttpMethod.PATCH, "/pedido/*/status")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/pedido/*")
                        .hasRole("ADMIN")




                        .requestMatchers(HttpMethod.POST, "/itempedido")
                        .hasRole("CLIENTE")

                        .requestMatchers(HttpMethod.GET, "/itempedido")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/itempedido/**")
                        .hasAnyRole("ADMIN", "CLIENTE")

                        .requestMatchers(HttpMethod.DELETE, "/itempedido/**")
                        .hasRole("CLIENTE")



                        .requestMatchers(HttpMethod.POST, "/pagamento")
                        .hasRole("CLIENTE")

                        .requestMatchers(HttpMethod.GET, "/pagamento")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/pagamento/*")
                        .hasAnyRole("ADMIN", "CLIENTE")

                        .requestMatchers(HttpMethod.GET, "/pagamento/pedido/**")
                        .hasAnyRole("ADMIN", "CLIENTE")

                        .requestMatchers(HttpMethod.DELETE, "/pagamento/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
