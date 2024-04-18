package com.shop.configuration;

import com.shop.filters.JwtTokenFilter;
import com.shop.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests -> {
                   requests.requestMatchers(
                           String.format("%s/users/register",apiPrefix),
                           String.format("%s/users/login",apiPrefix),
                           String.format("%s/orders/payment-callback",apiPrefix),
                           "/swagger-ui/index.html",
                           String.format("%s/v3/api-docs/**",apiPrefix)
                   ).permitAll()
                           .requestMatchers(HttpMethod.POST,String.format("%s/users/details/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                           .requestMatchers(HttpMethod.PUT,String.format("%s/users/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                           .requestMatchers(HttpMethod.DELETE,String.format("%s/users/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)

                           .requestMatchers(HttpMethod.GET,String.format("%s/categories/**",apiPrefix)).permitAll()
                           .requestMatchers(HttpMethod.POST,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.PUT,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.DELETE,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)

                           .requestMatchers(HttpMethod.GET,String.format("%s/products/**",apiPrefix)).permitAll()
                           .requestMatchers(HttpMethod.POST,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.PUT,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.DELETE,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)

                           .requestMatchers(HttpMethod.GET,String.format("%s/colors/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.POST,String.format("%s/colors/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.PUT,String.format("%s/colors/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.DELETE,String.format("%s/colors/**",apiPrefix)).hasRole(Role.ADMIN)

                           .requestMatchers(HttpMethod.GET,String.format("%s/sizes/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.POST,String.format("%s/sizes/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.PUT,String.format("%s/sizes/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.DELETE,String.format("%s/sizes/**",apiPrefix)).hasRole(Role.ADMIN)

                           .requestMatchers(HttpMethod.GET,String.format("%s/orders/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
//                           .requestMatchers(new AntPathRequestMatcher( apiPrefix + "/orders/payment-callback")).permitAll()
                           .requestMatchers(HttpMethod.GET,String.format("%s/orders/payment-callback",apiPrefix)).permitAll()
                           .requestMatchers(HttpMethod.POST,String.format("%s/orders/**",apiPrefix)).hasRole(Role.USER)
                           .requestMatchers(HttpMethod.PUT,String.format("%s/orders/**",apiPrefix)).hasRole(Role.ADMIN)
                           .requestMatchers(HttpMethod.DELETE,String.format("%s/orders/**",apiPrefix)).hasRole(Role.ADMIN)

//                           .requestMatchers(HttpMethod.PUT,String.format("%s/order-details/**",apiPrefix)).hasRole(Role.ADMIN)
//                           .requestMatchers(HttpMethod.POST,String.format("%s/order-details/**",apiPrefix)).hasAnyRole(Role.USER)
//                           .requestMatchers(HttpMethod.DELETE,String.format("%s/order-details/**",apiPrefix)).hasRole(Role.ADMIN)
//                           .requestMatchers(HttpMethod.GET,String.format("%s/order-details/**",apiPrefix)).hasAnyRole(Role.ADMIN,Role.USER)
                           .anyRequest().authenticated();

                }))
                .csrf(AbstractHttpConfigurer::disable);
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}
