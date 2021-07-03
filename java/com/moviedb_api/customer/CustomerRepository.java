package com.moviedb_api.customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("CustomerRepository")
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findById(Integer id);

    Page<Customer> findByFirstName(String first_name, Pageable page);

    Page<Customer> findByLastName(String last_name, Pageable page);

    Page<Customer> findByPostcode(String postcode, Pageable page);

    Page<Customer> findAll(Pageable page);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByEmailAndPassword(String email, String password);

}