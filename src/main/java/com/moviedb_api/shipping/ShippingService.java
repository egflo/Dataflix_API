package com.moviedb_api.shipping;

import com.moviedb_api.HttpResponse;
import com.moviedb_api.address.Address;
import com.moviedb_api.address.AddressRepository;
import com.moviedb_api.address.AddressRequest;
import com.moviedb_api.user_address.User_Address;
import com.moviedb_api.user_address.User_AddressRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShippingService {

    private final ShippingRepository shippingRepository;

    private final User_AddressRepository user_addressRepository;

    public ShippingService(ShippingRepository shippingRepository
            , User_AddressRepository user_addressRepository) {
        this.shippingRepository = shippingRepository;
        this.user_addressRepository = user_addressRepository;
    }


    public ResponseEntity<?> getAddress(Integer id) {
        Optional<Shipping> address = shippingRepository.findById(id);
        if (address.isPresent()) {
            return ResponseEntity.ok(address.get());
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Address not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getAddressByCustomerId(Integer id) {
        Iterable<Shipping> address = shippingRepository.findShippingByCustomerId(id);

        return ResponseEntity.ok(address);
    }

    public ResponseEntity<?> getAddressByOrderId(Integer id) {

        Optional<Shipping> address = shippingRepository.findShippingByOrderId(id);
        if (address.isPresent()) {
            return ResponseEntity.ok(address.get());
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Address not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> createAddress(ShippingRequest addressRequest) {
        Shipping address = new Shipping();
        address.setFirstname(addressRequest.getFirstname());
        address.setLastname(addressRequest.getLastname());
        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setPostcode(addressRequest.getPostcode());

        Shipping newAddress = shippingRepository.save(address);
        return ResponseEntity.ok(newAddress);
    }



    public ResponseEntity<?> updateAddressByOrderId(Integer id, ShippingRequest addressRequest) {
        Optional<Shipping> address = shippingRepository.findShippingByOrderId(id);
        if (address.isPresent()) {
            Shipping newAddress = address.get();
            newAddress.setFirstname(addressRequest.getFirstname());
            newAddress.setLastname(addressRequest.getLastname());
            newAddress.setStreet(addressRequest.getStreet());
            newAddress.setCity(addressRequest.getCity());
            newAddress.setState(addressRequest.getState());
            newAddress.setPostcode(addressRequest.getPostcode());

            return ResponseEntity.ok(shippingRepository.save(newAddress));
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Shipping with Order ID not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return ResponseEntity.notFound().build();

    }
    public ResponseEntity<?> updateAddress(ShippingRequest addressRequest) {
        Optional<Shipping> address = shippingRepository.findShippingById(addressRequest.getId());
        if (address.isPresent()) {
            Shipping newAddress = address.get();
            newAddress.setFirstname(addressRequest.getFirstname());
            newAddress.setLastname(addressRequest.getLastname());
            newAddress.setStreet(addressRequest.getStreet());
            newAddress.setCity(addressRequest.getCity());
            newAddress.setState(addressRequest.getState());
            newAddress.setPostcode(addressRequest.getPostcode());

            return ResponseEntity.ok(shippingRepository.save(newAddress));
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Address not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<?> deleteAddress(Integer id) {
        Optional<Shipping> address = shippingRepository.findShippingById(id);
        if (address.isPresent()) {

            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setMessage("Address deleted");
            httpResponse.setStatus(HttpStatus.OK.value());
            httpResponse.setSuccess(true);

            shippingRepository.deleteById(id);

            return ResponseEntity.ok(httpResponse);
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Address not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getAllAddresses(PageRequest pageRequest) {

        return ResponseEntity.ok(shippingRepository.findAll(pageRequest));
    }


    public ResponseEntity<?> getAddressesByFirstName(String firstName, PageRequest pageRequest) {
        return ResponseEntity.ok(
            shippingRepository.findAllByFirstname(firstName, pageRequest));
    }

    public ResponseEntity<?> getAddressesByLastName(String lastname, PageRequest pageRequest) {
        return ResponseEntity.ok(
                shippingRepository.findAllByLastname(lastname, pageRequest));
    }

    public ResponseEntity<?> getAddressesByCity(String city, PageRequest pageRequest) {
        return ResponseEntity.ok(
                shippingRepository.findAllByCity(city, pageRequest));
    }

    public ResponseEntity<?> getAddressesByState(String state, PageRequest pageRequest) {
        return ResponseEntity.ok(
                shippingRepository.findAllByState(state, pageRequest));
    }

    public ResponseEntity<?> getAddressesByPostcode(String postcode, PageRequest pageRequest) {
        return ResponseEntity.ok(
                shippingRepository.findAllByPostcode(postcode, pageRequest));
    }
}
