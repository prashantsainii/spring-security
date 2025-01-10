package com.prashant.config;

import com.prashant.exceptionhandling.CustomAccessDeniedHandler;
import com.prashant.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.prashant.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("prod")
public class ProjectSecurityProdConfig {

    // from SpringBootWebSecurityConfiguration.java
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        // This will help to read the token which came as part of request header. The read value is populated as part of request attribute or parameter. This will further help CsrfFilter to token comparison
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  //  generate jwt token
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("https://localhost:4200"));  // this will not work because our ui will be available on http only not https  // all the origins which we want to allow
                        config.setAllowedMethods(Collections.singletonList("*"));  // which all methods(GET, POST, etc) we want to allow
                        config.setAllowCredentials(true);  // allow credentials sharing
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));  // as we will be sending headers as part of response. In response header authorization our token will be here
                        config.setMaxAge(3600L);  // time is seconds
                        return config;
                    }
                }))
                .csrf(csrfConfig -> csrfConfig
                        .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/contact", "/register","/apiLogin") // telling spring sec to ignore the csrf protection for these endpoints
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .requiresChannel(rcc -> rcc.anyRequest().requiresSecure())  // only HTTPS requests will be allowed, if this configuration is not added both HTTP and HTTPS will be allowed
                .authorizeHttpRequests(requests -> requests
//                        .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
//                        .requestMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")
//                        .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
//                        .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                        .requestMatchers("/myAccount").hasRole("USER")
                        .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/myLoans").authenticated()
                        .requestMatchers("/myCards").hasRole("USER")
                        .requestMatchers("/user").authenticated()
                        .requestMatchers("/contact","/notices", "/register", "/invalidSession", "/error", "/apiLogin").permitAll());   // all requests apart from this will act like -> requests.anyRequest().denyAll()
        http.formLogin(withDefaults());  // uses UsernamePasswordAuthenticationFilter.java class's  attemptAuthentication() method
//        http.formLogin(flc -> flc.____) // no method to configure ui based authentication through CustomBasicAuthenticationEntryPoint
        http.httpBasic(hbc -> hbc.authenticationEntryPoint((new CustomBasicAuthenticationEntryPoint())));  // we are using our custom authentication entry point
//        http.exceptionHandling(ehc-> ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));  // it is global config as in case when apart from authentication spring security may throw 401 error so to handle such case we can have it
        http.exceptionHandling(ehc-> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));  // this will be triggered at global level

        // uses BasicAuthenticationFilter.java class's  doFilterInternal() method
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

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        EazyBankProdUsernamePwdAuthenticationProvider authenticationProvider =
                new EazyBankProdUsernamePwdAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return  providerManager;
    }

}
