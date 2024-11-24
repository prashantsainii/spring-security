package com.prashant.repository;

import com.prashant.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// if we were to use traditional JDBC code we have to write a lot of code to map database column and pojo class
@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {   // Customer - show that Crud operations has to be performed on which entity class i.e. Customer(in our case). Long - datatype of primary key

    Optional<Customer> findByEmail(String email);
}
