package com.moviedb_api.roles;

import com.moviedb_api.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("RoleRepository")
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Optional<Role> findById(Integer id);

    Optional<Role> findByName(String id);
}