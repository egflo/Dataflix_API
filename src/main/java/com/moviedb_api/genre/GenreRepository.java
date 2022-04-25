package com.moviedb_api.genre;


import com.moviedb_api.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("GenreRepository")
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Page<Genre> findByName(String name, Pageable pageable);


    Page<Genre> findAll(Pageable pageable);
}