package com.prashant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfig {

    // from SpringBootWebSecurityConfiguration.java
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());  // will perform check on all the api endpoints
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());  // will allow all the api to provide output without any check
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());   // will throw 403 error after the authentication
        http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())  // only HTTP requests will be allowed, if this configuration is not added both HTTP and HTTPS will be allowed
                .csrf(csrfConfig -> csrfConfig.disable()) // spring security will enforce the csrf protection on routes which perform write operations, so we are disabling them for a while
                .authorizeHttpRequests((requests) -> requests.
                requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated()
                .requestMatchers("/contact","/notices", "/register").permitAll());   // all requests apart from this will act like -> requests.anyRequest().denyAll()
        http.formLogin(withDefaults());  // uses UsernamePasswordAuthenticationFilter.java class's  attemptAuthentication() method
        http.httpBasic(withDefaults());  // uses BasicAuthenticationFilter.java class's  doFilterInternal() method
//        http.formLogin(flc -> flc.disable());
//        http.httpBasic(hbc -> hbc.disable());
        return http.build();
    }

//    not required as we have our Custom Bean
//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataScource) {  // spring boot creates the object of DataSource(we just have to use it) because of the details we have mentioned in the application.properties file
//        return new JdbcUserDetailsManager(dataScource);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public CompromisedPasswordChecker compromisedPasswordChecker() {
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }

}
