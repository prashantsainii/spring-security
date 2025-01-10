package com.prashant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

// The authorizeHttpRequests() method is a key part of the Spring Security framework,
// used to configure HTTP request-based security rules. It is part of the HttpSecurity
// class and allows you to define granular access control policies for different
// URL patterns in a web application.
@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(request -> request
                            .requestMatchers("/secure").authenticated()
                            .anyRequest().permitAll())
                    .formLogin(Customizer.withDefaults())
                    .oauth2Login(Customizer.withDefaults());
        return httpSecurity.build();
    }

    // Not required to configure here, we are configuring it in application.properties file

    // To give clue to spring security about which Auth Server we are going to use i.e. Social login(google,github) or
    // our own Auth Server, we have to create ClientRegistrationRepository(which will store info about AuthSever)
//    @Bean
//    ClientRegistrationRepository clientRegistrationRepository() {
//        ClientRegistration github = githubClientRegistration();
//
////        IMCRR(c) stores a collection of ClientRegistration objects in memory. These objects represent the configuration
////        details of OAuth2 clients (e.g., client ID, client secret, authorization server, scopes, redirect URIs, etc.).
//        return new InMemoryClientRegistrationRepository(github);
//    }
//
//
//    private ClientRegistration githubClientRegistration() {
//        return CommonOAuth2Provider.GITHUB.getBuilder("github")
//                    .clientId("Ov23li5bkLicAvsyq3xt")
//                    .clientSecret("fc9422ed06198325bb25ca7643e06209ebf57eda").build();
//    }

}
