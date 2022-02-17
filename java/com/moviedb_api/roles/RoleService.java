package com.moviedb_api.roles;

import com.moviedb_api.customer.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class RoleService {

    @PersistenceContext
    private EntityManager entityManager;

    public void insertWithQuery(Integer userId, Integer roleId) {
        entityManager.createNativeQuery("INSERT INTO users_roles (userId, roleId) VALUES (?,?)")
                .setParameter(1, userId)
                .setParameter(2, roleId)
                .executeUpdate();
    }
}
