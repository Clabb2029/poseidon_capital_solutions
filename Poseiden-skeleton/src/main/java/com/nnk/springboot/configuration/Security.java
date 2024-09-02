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

/**
 * Security class to implement Spring Security
 *
 * @author Clara SLYS
 */
@Configuration
@EnableWebSecurity
public class Security {

    @Autowired
    private UserRepository userRepository;

    private Authentication authentication;

    /**
     * securityFilterChain method to:
     * manage permissions depending on user's role
     * prevent not logged-in user to access application
     * prevent an average user from accessing sensitive data
     * redirect on appropriate page depending on logged-in user's role
     * redirect to error page if an error occurs or if user is not authorized to access a page
     * destroy session when user logs out
     *
     * @param http HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     * @author Clara SLYS
     */
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

    /**
     * authenticationProvider method to retrieve the user details
     *
     * @return an authenticated object with full credentials
     * @author Clara SLYS
     */
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(new CustomUserDetailsService(userRepository));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * userDetailsService method used by authenticationProvider method to retrieve user's username, password, full name and role during authentication
     *
     * @return a CustomUserDetailService instance containing user's information
     * @author Clara SLYS
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    /**
     * passwordEncoder method to create a BCryptPasswordEncoder to encode user's password, so there is no sensitive data leak
     *
     * @return a PasswordEncoder instance
     * @author Clara SLYS
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * containerCustomizer method to redirect to a given page if url doesn't exist to prevent error page
     * Redirection page depends on logged-in user's role
     *
     * @return a WebServerFactoryCustomizer instance
     * @author Clara SLYS
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        if (authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/app/secure/article-details"));
        }
        return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/bidList/list"));
    }
}
