package com.moviedb_api.shipping;


import com.moviedb_api.sale.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("ShippingRepository")
public interface ShippingRepository extends CrudRepository<Shipping, Integer> {

    Page<Shipping> findAll(Pageable pageable);

    Optional<Shipping> findShippingById(Integer id);

    Iterable<Shipping> findShippingByCustomerId(Integer id);

    Optional<Shipping> findShippingByOrderId(Integer id);


    Page<Shipping> findAllByFirstname(String firstName, Pageable pageable);

    Page<Shipping> findAllByLastname(String lastName, Pageable pageable);

    Page<Shipping> findAllByStreet(String address, Pageable pageable);

    Page<Shipping> findAllByCity(String city, Pageable pageable);

    Page<Shipping> findAllByState(String state, Pageable pageable);

    Page<Shipping> findAllByPostcode(String zipcode, Pageable pageable);

}