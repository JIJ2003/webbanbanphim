package com.keycraft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.keycraft.service.CustomUserDetailsService;
import com.keycraft.model.User;
import com.keycraft.repository.UserRepository;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 @Autowired
	    private CustomUserDetailsService userDetailsService;
	 @Autowired
	 private com.keycraft.repository.UserRepository userRepository;
	 
	 @Bean
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	     http
	        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Bắt buộc
	         .csrf(csrf -> csrf.disable())
	         .authorizeHttpRequests(authz -> authz
	             .requestMatchers("/api/products/**", "/api/services/**", "/api/auth/**", "/auth/**").permitAll()
	             .requestMatchers("/", "/login", "/signup", "/cart", "/checkout", "/client/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
	             .requestMatchers("/dashboard").hasRole("ADMIN")
	             .anyRequest().permitAll()
	         )
	         .formLogin(form -> form
	             .loginPage("/login") // JSP login page
	             .loginProcessingUrl("/auth/login") // xử lý POST form
	             
	             .successHandler((request, response, authentication) -> {
	                 // Lấy email
	                 String email = authentication.getName();
	                 User user = userRepository.findByEmail(email).orElse(null);
	                 if (user != null) {
	                     request.getSession().setAttribute("currentUser", user);
	                 }
	                 // Redirect mặc định
	                 response.sendRedirect("/dashboard");  // hoặc "/index" tuỳ role
	             })
	             .permitAll()
	         )
	         .logout(logout -> logout
	             .logoutUrl("/auth/logout")
	             .logoutSuccessUrl("/login?logout=true")
	             .permitAll()
	         )
	         .headers(headers -> headers
	             .frameOptions().deny()
	         )
	         .sessionManagement(session -> session
	             .maximumSessions(1)
	             .maxSessionsPreventsLogin(false)
	         );

	     return http.build();
	 }
	 @Bean
	 public CorsConfigurationSource corsConfigurationSource() {
	     CorsConfiguration config = new CorsConfiguration();
	     config.setAllowCredentials(false); // ✅ Có thể giữ true
	     config.setAllowedOriginPatterns(List.of("http://localhost:8080")); // ✅ Đúng cách thay thế "*"
	     config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	     config.setAllowedHeaders(List.of("*"));

	     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	     source.registerCorsConfiguration("/**", config);
	     return source;
	 }

	 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return CookieHttpSessionIdResolver.withCookieName("JSESSIONID");
    }
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

   
}