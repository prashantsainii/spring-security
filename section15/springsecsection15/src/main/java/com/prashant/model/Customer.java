package com.prashant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

// we are not using DTO class - developers use DTO class to send data to UI instead of directly using entity classes

@Entity     // because of this annotation JPA framework will map(class name should be same as table name) the class name to table name inside database
@Table(name="customer")    // in case our database name is different from class name
@Getter @Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // indicates that id generation is already handled from database
    @Column(name = "customer_id")  // in case when column name is different from the field name(we can give whichever column we want to map the following field)
    private long id;

    private String name;

    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // we don't need to send the password to ui
    private String pwd;

    private String role;

    @Column(name = "create_dt")
    @JsonIgnore  // when we don't need to send a field to UI
    private Date createDt;

    // one record of customer can have multiple record in authorities table
    // when parent record(customer) is fetched, we want all children record(authority) as well to be loaded
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Authority> authorities;

}
