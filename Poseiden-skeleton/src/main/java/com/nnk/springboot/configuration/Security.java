package com.nnk.springboot.configuration;

import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class Security {

    @Autowired
    private UserRepository userRepository;

    private Authentication authentication;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/app/login", "/css/**").permitAll()
                                .requestMatchers("/", "/admin/home", "/user/**", "/app/secure/article-details").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/app/login")
                                .defaultSuccessUrl("/bidList/list", true)
                                .successHandler((request, response, authentication) -> {
                                    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                                    if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
                                        response.sendRedirect("/app/secure/article-details");
                                    } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))){
                                        response.sendRedirect("/bidList/list");
                                    }
                                })
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .accessDeniedHandler((request, response, accessDeniedException) -> response.sendRedirect("/app/error"))
                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/app-logout"))
                                .logoutSuccessUrl("/app/login?logout")
                                .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)
                );
        return http.build();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(new CustomUserDetailsService(userRepository));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        if (authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/app/secure/article-details"));
        }
        return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/bidList/list"));
    }
}
