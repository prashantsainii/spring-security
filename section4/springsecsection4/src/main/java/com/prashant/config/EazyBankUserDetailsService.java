package com.prashant.config;

import com.prashant.model.Customer;
import com.prashant.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// this is custom UserDetailsService implementation so spring security will know that
// we are trying to use our Custom UDS instead of InMemoryUDS or JdbcUDS

@Service    // bean of this class will be created
@RequiredArgsConstructor   // this anno. will create the constructor with required field and autowire it automatically
public class EazyBankUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    /**
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username here is received from DaoAuthentication Provider class
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new
                UsernameNotFoundException("User details not found for the user:" + username));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
        return new User(customer.getEmail(), customer.getPwd(), authorities);
    }
}
