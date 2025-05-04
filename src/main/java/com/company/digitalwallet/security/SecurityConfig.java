package com.company.digitalwallet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

import com.company.digitalwallet.dto.enums.Role;
import com.company.digitalwallet.model.Customer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/customer").permitAll()
                        .requestMatchers(HttpMethod.GET, "/wallet/listWallets/{customerId}").access(
                                (authentication, context) -> {
                                    Customer user = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                                    Long pathCustomerId = Long.parseLong(context.getVariables().get("customerId"));
                                    boolean isSelf = user.getId().equals(pathCustomerId);
                                    boolean isEmployee = user.getAuthorities().stream()
                                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + Role.EMPLOYEE));
                                    return new AuthorizationDecision(isSelf || isEmployee);
                                }
                        )
                        .requestMatchers(HttpMethod.POST, "/wallet/listWallets/{customerId}").access(
                                (authentication, context) -> {
                                    Customer user = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                                    Long pathCustomerId = Long.parseLong(context.getVariables().get("customerId"));
                                    boolean isSelf = user.getId().equals(pathCustomerId);
                                    boolean isEmployee = user.getAuthorities().stream()
                                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + Role.EMPLOYEE));
                                    return new AuthorizationDecision(isSelf || isEmployee);
                                }
                        )
                        .requestMatchers("/wallet/**").authenticated()
                        .requestMatchers("/transaction/**").authenticated()
                        .requestMatchers("/approve/**").hasRole(Role.EMPLOYEE.name())
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .httpBasic(withDefaults());

        return http.build();
    }


}


