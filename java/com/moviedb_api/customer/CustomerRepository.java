package com.moviedb_api.customer;
import com.moviedb_api.sale.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.Optional;

@Repository("CustomerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findById(Integer id);

    Page<Customer> findCustomerByFirstname(String first_name, Pageable page);

    Page<Customer> findCustomerByLastname(String last_name, Pageable page);

    Page<Customer> findAll(Pageable page);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByEmailAndPassword(String email, String password);

    Boolean existsCustomerById(Integer id);

    Page<Customer> findCustomerByFirstnameContainingOrLastnameContainingOrEmailContaining(String firstname, String lastname, String Email, Pageable page);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.created <= ?1")
    long findAllActiveUsersFromDate(Date date);

    @Query("SELECT c FROM Customer c WHERE c.id =?1")
    Page<Customer> findByCustomerId(Integer id, PageRequest pageable);
}