package com.moviedb_api.star;

import com.moviedb_api.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("StarRepository")
public interface StarRepository extends JpaRepository<Star, String> {
    Iterable<Star> findByName(String name);

    @Query(value = "SELECT * FROM stars WHERE name LIKE %?1% LIMIT 10", nativeQuery = true)
    List<Star> findByNameLike(String name);

    @Query(value = "SELECT * FROM stars WHERE name LIKE %?1%", nativeQuery = true)
    Page<Star> findByNameLike(String name, Pageable pageable);
}