package com.prashant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity     // because of this annotation JPA framework will map(class name should be same as table name) the class name to table name inside database
@Table(name="customer")    // in case our database name is different from class name
@Getter @Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // indicates that id generation is already handled from database
    private long id;
    private String email;
    private String pwd;
    @Column(name="role")   // in case when column name is different from the field name(we can give whichever column we want to map the following field)
    private String role;



}
