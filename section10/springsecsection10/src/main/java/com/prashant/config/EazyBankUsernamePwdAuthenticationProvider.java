package com.prashant.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// will be the only provider class as of now, as other provider implementations will back off

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class EazyBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;   // Bean which will be used as UserDetailsService implementation is EazyBankUserDetailsService
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // if the user is not found our EazyBankUDS class will throw AuthenticationException

        return new UsernamePasswordAuthenticationToken(username,pwd,userDetails.getAuthorities());

//        if(passwordEncoder.matches(pwd,userDetails.getPassword())) {
//            // fetch age details and perform validation to check if ag > 18
//            return new UsernamePasswordAuthenticationToken(username,pwd,userDetails.getAuthorities());
//        }
//        else {
//            throw new BadCredentialsException("Invalid password");
//        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
