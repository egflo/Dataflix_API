package com.moviedb_api.star;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("StarRepository")
public interface StarRepository extends CrudRepository<Star, String> {

    Iterable<Star> findByName(String name);
}