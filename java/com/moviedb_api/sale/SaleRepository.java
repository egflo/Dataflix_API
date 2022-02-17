package com.moviedb_api.sale;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository("SaleRepository")
public interface SaleRepository extends CrudRepository<Sale, Integer> {

    Page<Sale> findAll(Pageable pageable);

    Optional<Sale> findByIdAndCustomerId(Integer saleId, Integer customerId);

    Page<Sale> findSaleByCustomerId(Integer id, Pageable pageable);

    @Query("SELECT SUM(s.subTotal) FROM Sale s")
    Double findTotalSales();

    //@Query("SELECT COUNT(s) FROM Sale s WHERE DAY(s.salesDate) = ?1")
    //@Query(value = "SELECT COUNT(*) FROM Sales s WHERE DATE(s.saleDate) >=  DATE(NOW()) - INTERVAL ?1 DAY", nativeQuery = true)
    @Query(value = "SELECT COUNT(*) FROM sales WHERE DATE (saleDate) = ?1", nativeQuery = true)
    long findAllSalesFromDate(Date date);

    @Query(value = "SELECT COUNT(*) FROM sales WHERE device = ?1", nativeQuery = true)
    long findAllSalesFromDevice(String device);

    @Query(value = "SELECT COUNT(*) FROM sales WHERE MONTH (saleDate) = ?1 AND YEAR(saleDate) =?2", nativeQuery = true)
    long findAllSalesFromMonthAndYear(Integer year, Integer month);


    //@Query(value = "SELECT * FROM sales INNER JOIN shipping ON shipping.customerId = sales.customerId WHERE sales.id = ?1 OR sales.status =?1 OR sales.customerId =?1", nativeQuery = true)
    @Query("SELECT s FROM Sale s WHERE s.status =?1" +
            "OR s.shipping.firstname LIKE %?1% OR s.shipping.lastname LIKE %?1% " +
            "OR s.shipping.street LIKE %?1% OR s.shipping.city =?1")
    Page<Sale> findAllSalesFromString(String search, PageRequest pageable);

    @Query("SELECT s FROM Sale s WHERE s.status =?1" +
            "OR s.shipping.firstname LIKE %?1% OR s.shipping.lastname LIKE %?1% " +
            "OR s.shipping.street LIKE %?1% OR s.shipping.city =?1 AND s.status =?2")
    Page<Sale> findAllSalesFromStringAndStatus(String search, String status, PageRequest pageable);

    @Query("SELECT s FROM Sale s WHERE s.customerId =?1 OR s.id =?1")
    Page<Sale> findSaleByIdOrCustomerId(Integer id, PageRequest pageable);

    @Query("SELECT s FROM Sale s WHERE s.customerId =?1 OR s.id =?1 AND s.status =?2")
    Page<Sale> findSaleByIdOrCustomerIdAndStatus(Integer id, String status, PageRequest pageable);

    Page<Sale> findAllByStatus(String status, Pageable pageable);

}