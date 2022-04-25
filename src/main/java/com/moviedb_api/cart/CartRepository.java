package com.moviedb_api.cart;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findAllByUserIdOrderByCreatedDateDesc(String userId);

    List<Cart> findAllByUserId(String userId);

    Page<Cart> findAllByMovieId(String movieId, Pageable pageable);

    Page<Cart> findAllByUserId(Integer userId, Pageable pageable);

    Optional<Cart> findCartById(Integer id);

    Optional<Cart> findCartByUserIdAndMovieId(String userId, String movieId);

}
