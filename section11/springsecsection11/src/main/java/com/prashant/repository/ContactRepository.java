package com.prashant.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prashant.model.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {


}