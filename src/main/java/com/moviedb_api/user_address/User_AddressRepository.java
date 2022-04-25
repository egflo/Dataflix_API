package com.moviedb_api.user_address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("User_AddressRepository")
public interface User_AddressRepository extends JpaRepository<User_Address, Integer> {

    List<User_Address> findUser_AddressByUserId(Integer id);

    Optional<User_Address> findByAddressId(Integer id);

    Optional<User_Address> findByUserIdAndAddressId(Integer userId, Integer addressId);

    List<User_Address> findByUserId(Integer id);

    void deleteByUserIdAndAddressId(Integer userId, Integer addressId);

    void deleteByAddressId(Integer addressId);

    void deleteByUserId(Integer userId);
}
