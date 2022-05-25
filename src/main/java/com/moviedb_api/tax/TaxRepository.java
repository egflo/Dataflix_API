package com.moviedb_api.tax;


import com.moviedb_api.sale.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("TaxRepository")
public interface TaxRepository extends CrudRepository<TaxRate, Integer> {

    Page<TaxRate> findAll(Pageable pageable);

    Optional<TaxRate> findById(Integer id);

    Iterable<TaxRate> findByCityContaining(String city);

    Iterable<TaxRate> findByCityContainingIgnoreCase(String city);

    @Query("select u from TaxRate u where lower(u.city) like lower(concat('%', :name,'%'))")
    Iterable<TaxRate> findByCity(String name);

}