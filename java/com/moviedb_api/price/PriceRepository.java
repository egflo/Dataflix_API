package com.moviedb_api.price;

import com.moviedb_api.movie.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@Repository("PriceRepository")
public interface PriceRepository extends CrudRepository<Price, String> {

    Optional<Price> findPriceByMovieId(String id);
}
