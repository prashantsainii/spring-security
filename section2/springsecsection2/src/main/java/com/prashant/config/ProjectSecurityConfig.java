package com.prashant.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    // from SpringBootWebSecurityConfiguration.java
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());  // will perform check on all the api endpoints
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());  // will allow all the api to provide output without any check
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());   // will throw 403 error after the authentication
        http.authorizeHttpRequests((requests) -> requests.
                requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated()
                .requestMatchers("/contact","/notices").permitAll());   // all requests apart from this will act like -> requests.anyRequest().denyAll()
        http.formLogin(withDefaults());  // uses UsernamePasswordAuthenticationFilter.java class's  attemptAuthentication() method
        http.httpBasic(withDefaults());  // uses BasicAuthenticationFilter.java class's  doFilterInternal() method
//        http.formLogin(flc -> flc.disable());
//        http.httpBasic(hbc -> hbc.disable());
        return http.build();
    }
}
