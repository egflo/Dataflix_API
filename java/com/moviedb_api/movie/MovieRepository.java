package com.moviedb_api.movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@Repository("MovieRepository")
public interface MovieRepository extends JpaRepository<Movie, String> {

    @Query(value = "SELECT * FROM movies WHERE title LIKE %:title% LIMIT 5", nativeQuery = true)
    List<Movie> findByTitleLike(String title);

    Page<Movie> findByTitleContaining(String title, Pageable pageable);

    //List<Movie> findByYear(Integer year);
    Page<Movie> findByYear(Integer year, Pageable pageable);

    //Iterable<Movie> findAllByRated(String rated);
    Page<Movie> findByRated(String rated, Pageable pageable);

   // @Query(value = "SELECT * FROM movies m WHERE m.id = ?1 OR m.title CONTAINS %?1%", nativeQuery = true)
    Page<Movie> findMoviesByIdOrTitleContaining(String id, String title, Pageable pageable);

    //@Query(value = "SELECT m FROM Movie m INNER JOIN m.genres gm INNER JOIN gm.genre g WHERE g.name = :name ")
    @Query(value = "SELECT m FROM Movie m INNER JOIN m.genres g WHERE g.name = :name ")
    Page<Movie> findMovieByGenreName(String name, Pageable pageable);

    @Query(value = "SELECT m FROM Movie m INNER JOIN m.genres g  WHERE g.id = :id ") //WHERE g.name = :name
    Page<Movie> findMovieByGenreId(Integer id, Pageable pageable);

    @Query(value = "SELECT m FROM Movie m INNER JOIN m.cast cm INNER JOIN cm.star s WHERE s.starId = :id ") //WHERE g.name = :name
    Page<Movie> findMovieByStarId(String id, Pageable pageable);

   @Query(value = "SELECT m FROM Movie m INNER JOIN m.genres g WHERE g.name = :name OR m.title LIKE %:name%") //WHERE g.name = :name
    Page<Movie> findMovieByGenreNameOrTitleLike(String name, Pageable pageable);
}
