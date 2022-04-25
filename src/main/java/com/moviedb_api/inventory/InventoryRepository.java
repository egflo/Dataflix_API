package com.moviedb_api.inventory;

import com.moviedb_api.movie.Movie;
import com.moviedb_api.price.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@Repository("InventoryRepository")
public interface InventoryRepository extends CrudRepository<Inventory, String> {

    @Query(value = "SELECT i FROM Inventory i WHERE i.productId = ?1 OR i.movie.title LIKE %?1%", nativeQuery = false)
    Page<Inventory> findMoviesByIdOrTitleContaining(String search, Pageable pageable);

    @Query(value = "SELECT i FROM Inventory i WHERE i.productId = ?1 OR i.movie.title LIKE %?1% AND i.status =?2", nativeQuery = false)
    Page<Inventory> findMoviesByIdOrTitleContainingAndStatus(String search, String status, Pageable pageable);

    Optional<Inventory> findInventoryByProductId(String productId);

    Page<Inventory> findAll(Pageable pageable);
    Page<Inventory> findInventoryByStatus(String status, Pageable pageable);
}
