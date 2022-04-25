package com.moviedb_api.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    @Override
    Optional<RefreshToken> findById(Integer id);
    Optional<RefreshToken> findByToken(String token);

    int deleteByUserId(Integer userId);
}