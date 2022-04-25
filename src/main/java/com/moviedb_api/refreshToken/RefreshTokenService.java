package com.moviedb_api.refreshToken;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.moviedb_api.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private CustomerRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    public RefreshToken createRefreshToken(Integer userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiration(Instant.now().plusMillis(3600000));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiration().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public RefreshToken verifyExpiration(String token) {
        Optional<RefreshToken> optional = refreshTokenRepository.findByToken(token);
        if(optional.isPresent()) {
            RefreshToken refreshToken = optional.get();
            if (refreshToken.getExpiration().compareTo(Instant.now()) < 0) {
                refreshTokenRepository.delete(refreshToken);
                throw new TokenRefreshException(refreshToken.getToken(), "Refresh token was expired. Please make a new signin request");
            }

            return refreshToken;
        }
        throw new TokenRefreshException(token, "Refresh token was not found");
    }
    @Transactional
    public int deleteByUserId(Integer userId) {
        return refreshTokenRepository.deleteByUserId(userId);
    }
}