package com.moviedb_api.stars_in_movies;

import com.moviedb_api.star.Star;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("Star_MovieRepository")
public interface Star_MovieRepository extends CrudRepository<Star_Movie, String> {
    Iterable<Star_Movie> findByStarId(String id);

    Iterable<Star_Movie> findByMovieId(String id);

}