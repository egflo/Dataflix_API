package com.moviedb_api.sale;


import com.moviedb_api.order.Order;
import com.moviedb_api.star.Star;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("SaleRepository")
public interface SaleRepository extends CrudRepository<Sale, Integer> {

    Page<Sale> findAll(Pageable pageable);

    Iterable<Sale> findSaleByCustomerId(Integer id, Sort sort);
}