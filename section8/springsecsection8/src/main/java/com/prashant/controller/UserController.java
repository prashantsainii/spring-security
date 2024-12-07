package com.prashant.controller;

import com.prashant.model.Customer;
import com.prashant.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

// createUser(UserDetails user) method of UserDetailsManager interface has some drawbacks
// 1 - we need to implement all other methods of UserDetailsManager interface which we may not be needed by the organization
// 2 - we will be restricted to input parameter of type UserDetails(so any other parameter as per our demand may not be supported)
// 3 - using REST API's to perform CRUD operations is more simple and dynamic
@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository; // using this repo only we are going to save new customer details into the database
    private final PasswordEncoder passwordEncoder;  // as we don't want to save plain text password


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        // When a client sends a POST request to the /register endpoint, the request's JSON body is deserialized into a Customer object.
        // the incoming JSON structure matches the fields of the Customer class. Getter Setter played important role in data binding

        // Customer is a JPA entity
        // in real project we never try to accept the request in form of entity classes we will always use DTO pattern

        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            customer.setCreateDt(new Date(System.currentTimeMillis()));
            Customer savedCustomer = customerRepository.save(customer);  // save method will help us to save the new record. JPA framework will take care of insert statements
                                                                         // and populating all the data we have inside input object we are trying to send
            if(savedCustomer.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Given user details are successfully registered");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User registration failed");
            }


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occurred: " + e.getMessage());
        }
    }

    @RequestMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(authentication.getName());
        return optionalCustomer.orElse(null);
    }

}
