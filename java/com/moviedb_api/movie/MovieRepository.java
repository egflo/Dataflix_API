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
    //List<Movie> findByTitle(String title);
    Page<Movie> findByTitleContaining(String title, Pageable pageable);

    //List<Movie> findByYear(Integer year);
    Page<Movie> findByYear(Integer year, Pageable pageable);

    //Iterable<Movie> findAllByRated(String rated);
    Page<Movie> findByRated(String rated, Pageable pageable);

    @Query(value = "SELECT m FROM Movie m INNER JOIN m.genres gm INNER JOIN gm.genre g WHERE g.name = :name ") //WHERE g.name = :name
    Page<Movie> findMovieByGenreName(String name, Pageable pageable);

    @Query(value = "SELECT m FROM Movie m INNER JOIN m.genres gm INNER JOIN gm.genre g WHERE g.id = :id ") //WHERE g.name = :name
    Page<Movie> findMovieByGenreId(Integer id, Pageable pageable);

    @Query(value = "SELECT m FROM Movie m INNER JOIN m.cast cm INNER JOIN cm.star s WHERE s.starId = :id ") //WHERE g.name = :name
    Page<Movie> findMovieByStarId(String id, Pageable pageable);

    //@Query(value = "SELECT m FROM Movie m INNER JOIN m.ratings cm INNER JOIN cm.rating s") //WHERE g.name = :name
    //Page<Movie> findByTopRated(Pageable pageable);
}
