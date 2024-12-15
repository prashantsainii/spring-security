package com.prashant.repository;

import com.prashant.model.Accounts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository  // Repositories are responsible for interacting with the database and performing CRUD (Create, Read, Update, Delete) operations on entities.
public interface AccountsRepository extends CrudRepository<Accounts, Long> {

    Accounts findByCustomerId(long customerId);

}