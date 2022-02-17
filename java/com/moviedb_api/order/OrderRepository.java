package com.moviedb_api.order;

import com.moviedb_api.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@Repository("OrderRepository")
public interface OrderRepository extends CrudRepository<Order, Integer> {
    Optional<Order> findById(Integer id);

    long countByMovieId(String id);

    Page<Order> findAll(Pageable pageable);

   // @Query(value = "SELECT COUNT(m.movieId) as count, m.movieId FROM Order m GROUP BY m.movieId ORDER BY count")
    //@Query(value = "SELECT new map(COUNT(m.movieId) AS sales, m.movieId AS movieId) FROM Order m GROUP BY m.movieId ORDER BY sales DESC ")
   //@Query(value = "SELECT COUNT(movieId) as sales, movieId FROM orders GROUP BY movieId ORDER BY sales DESC", nativeQuery = true)
   // @Query(value = "SELECT new map(COUNT(m.movieId) AS sales, m.movieId AS movieId) FROM Order m GROUP BY m.movieId ORDER BY sales DESC ")
   //@Query(value = "SELECT COUNT(movieId) as sales FROM orders as o GROUP BY movieId ORDER BY sales DESC", nativeQuery = true)
   @Query(value = "SELECT new map(COUNT(m.movieId) AS sales, m.movieId AS movieId) FROM Order m GROUP BY m.movieId ORDER BY sales DESC ")
   Page<Order> findBestSellers(Pageable pageable);
}
