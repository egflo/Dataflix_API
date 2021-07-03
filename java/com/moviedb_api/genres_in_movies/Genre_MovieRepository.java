package com.moviedb_api.genres_in_movies;

import com.moviedb_api.genre.Genre;
import com.moviedb_api.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("Genre_MovieRepository")
public interface Genre_MovieRepository extends JpaRepository<Genre_Movie, Integer> {

    Page<Genre_Movie> findByGenreName(String name, Pageable pageable);

    Page<Genre_Movie> findByGenreId(Integer id, Pageable pageable);

    @Query(value = "SELECT g FROM Genre g") //WHERE g.name = :name
    Iterable<Genre> getAllGenres();
}