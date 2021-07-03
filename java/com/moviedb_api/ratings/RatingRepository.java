package com.moviedb_api.ratings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("RatingRepository")
public interface RatingRepository extends CrudRepository<Rating, String> {
    Optional<Rating> findByMovieId(String id);

    Page<Rating> findAll(Pageable pageable);

    @Query(value = "SELECT new map(r.numVotes AS votes, r.movieId AS movieId) FROM Rating r ORDER BY r.numVotes DESC ")
    Page<Rating> findBestRated(Pageable pageable);

    @Query(value = "SELECT new map(r.rottenTomatoes AS rottenTomatoes, r.movieId AS movieId) FROM Rating r WHERE r.numVotes > 100000 ORDER BY r.rottenTomatoes DESC ")
    Page<Rating> findBestCritic(Pageable pageable);
}