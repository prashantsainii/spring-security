package com.prashant.config;

import com.prashant.exceptionhandling.CustomAccessDeniedHandler;
import com.prashant.exceptionhandling.CustomBasicAuthenticationEntryPoint;
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
        http.sessionManagement(smc->smc.invalidSessionUrl("/invalidSession").maximumSessions(3).maxSessionsPreventsLogin(true))  // will be triggered in case session times out, or tampered JSESSION ID value's + number of concurrent session's + if once logged in cannot create new session
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())  // only HTTP requests will be allowed, if this configuration is not added both HTTP and HTTPS will be allowed
                .csrf(csrfConfig -> csrfConfig.disable()) // spring security will enforce the csrf protection on routes which perform write operations, so we are disabling them for a while
                .authorizeHttpRequests((requests) -> requests.
                requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated()
                .requestMatchers("/contact","/notices", "/register", "/error", "/invalidSession").permitAll());   // all requests apart from this will act like -> requests.anyRequest().denyAll()
        http.formLogin(withDefaults());  // uses UsernamePasswordAuthenticationFilter.java class's  attemptAuthentication() method
//        http.formLogin(flc -> flc.____) // no method to configure ui based authentication through CustomBasicAuthenticationEntryPoint
        http.httpBasic(hbc -> hbc.authenticationEntryPoint((new CustomBasicAuthenticationEntryPoint())));  // we are using our custom authentication entry point
//        http.exceptionHandling(ehc-> ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));  // it is global config as in case when apart from authentication spring security may throw 401 error so to handle such case we can have it
        http.exceptionHandling(ehc-> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));  // this will be triggered at global level for all

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
