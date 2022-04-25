package com.moviedb_api.review;

import com.moviedb_api.ratings.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("ReviewRepository")
public interface ReviewRepository extends CrudRepository<Review, Integer> {

    Optional<Review> findReviewById(Integer id);

    Page<Review> findReviewByMovieId(String id, Pageable pageable);

    Page<Review> findReviewByCustomerId(Integer id, Pageable pageable);

    Page<Review> findAll(Pageable pageable);

}