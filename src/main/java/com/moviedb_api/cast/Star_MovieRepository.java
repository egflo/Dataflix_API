package com.moviedb_api.cast;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("Star_MovieRepository")
public interface Star_MovieRepository extends CrudRepository<Star_Movie, Integer> {
    List<Star_Movie> findByStarId(String id);
    List<Star_Movie> findByMovieId(String id);
    Set<Star_Movie> findByStarIdIn(Set<String> ids);
    Set<Star_Movie> findByMovieIdIn(Set<String> ids);
    Optional<Star_Movie> findByStarIdAndMovieId(String starId, String movieId);
}