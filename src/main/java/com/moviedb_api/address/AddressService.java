package com.moviedb_api.address;

import com.moviedb_api.HttpResponse;
import com.moviedb_api.bookmark.Bookmark;
import com.moviedb_api.bookmark.BookmarkRepository;
import com.moviedb_api.bookmark.BookmarkRequest;
import com.moviedb_api.customer.Customer;
import com.moviedb_api.customer.CustomerRepository;
import com.moviedb_api.security.AuthenticationFacade;
import com.moviedb_api.user_address.User_Address;
import com.moviedb_api.user_address.User_AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressService {

    @PersistenceContext
    EntityManager entityManager;

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final User_AddressRepository user_addressRepository;


    @Autowired
    private AuthenticationFacade authenticationFacade;

    public AddressService(AddressRepository addressRepository
            ,User_AddressRepository user_addressRepository
            ,CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.user_addressRepository = user_addressRepository;
        this.customerRepository = customerRepository;
    }

    public ResponseEntity<?> getAddress(Integer id) {
        HttpResponse httpResponse = new HttpResponse();

        if(authenticationFacade.hasRole("ADMIN")){
            Optional<Address> address = addressRepository.findById(id);
            if (address.isPresent()) {
                httpResponse.setStatus(HttpStatus.OK.value());
                httpResponse.setMessage("Address found");
                httpResponse.setData(address.get());
                return ResponseEntity.ok(httpResponse);
            }

            httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
            httpResponse.setMessage("Address not found");
            return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
        }


        //If user is not admin, check if user is the owner of the address
        Optional<User_Address> user_address = user_addressRepository.findByUserIdAndAddressId(authenticationFacade.getUserId(), id);

        if (user_address.isPresent()) {
            Address address = addressRepository.findById(id).get();
            httpResponse.setStatus(HttpStatus.OK.value());
            httpResponse.setMessage("Address found");
            httpResponse.setData(address);
            return ResponseEntity.ok(httpResponse);
        }

        httpResponse.setMessage("Address not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<?> createAddress(AddressRequest addressRequest) {

        Address address = new Address();
        address.setFirstname(addressRequest.getFirstname());
        address.setLastname(addressRequest.getLastname());
        address.setUnit(addressRequest.getUnit() == null ? addressRequest.getUnit(): "");
        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setPostcode(addressRequest.getPostcode());

        Address newAddress = addressRepository.save(address);

        User_Address user_address = new User_Address();
        //If user is not admin, set user id to the current user
        user_address.setUserId(authenticationFacade.hasRole("ADMIN") ? addressRequest.getUserId() : authenticationFacade.getUserId());
        user_address.setAddressId(newAddress.getId());

       //if (addresses.size() == 0) {
       //     Customer customer = customerRepository.findById(addressRequest.getUserId()).get();
      //      customer.setPrimaryAddress(newAddress.getId());
       //     customerRepository.save(customer);
      // }

        user_addressRepository.save(user_address);

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Address created successfully");
        httpResponse.setStatus(HttpStatus.OK.value());
        httpResponse.setSuccess(true);
        httpResponse.data = newAddress;

        return ResponseEntity.ok(httpResponse);
    }

    public ResponseEntity<?> updateAddress(AddressRequest addressRequest) {

        //If user is not admin, check if user is the owner of the address
        Optional<User_Address> user_address = user_addressRepository.findByUserIdAndAddressId(authenticationFacade.getUserId(), addressRequest.getId());

        if(authenticationFacade.hasRole("USER") && !user_address.isPresent()){
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setMessage("Address not found");
            httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
            httpResponse.setSuccess(false);
            return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
        }

        Optional<Address> address = addressRepository.findById(addressRequest.getId());

        if (address.isPresent()) {
            Address newAddress = address.get();
            newAddress.setFirstname(addressRequest.getFirstname());
            newAddress.setLastname(addressRequest.getLastname());
            newAddress.setStreet(addressRequest.getStreet());
            newAddress.setUnit(addressRequest.getUnit() == null ? addressRequest.getUnit() : "");
            newAddress.setCity(addressRequest.getCity());
            newAddress.setState(addressRequest.getState());
            newAddress.setPostcode(addressRequest.getPostcode());

            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setMessage("Address created successfully");
            httpResponse.setStatus(HttpStatus.OK.value());
            httpResponse.setSuccess(true);
            httpResponse.data = newAddress;

            return ResponseEntity.ok(httpResponse);
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Address not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);
        httpResponse.data = null;
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<?> deleteAddress(Integer id) {

        //If user is not admin, check if user is the owner of the address
        Optional<User_Address> user_address = user_addressRepository.findByUserIdAndAddressId(authenticationFacade.getUserId(), id);
        List<User_Address> user_addresses = user_addressRepository.findByUserId(id);

        if(authenticationFacade.hasRole("USER") && !user_address.isPresent()){
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setMessage("Address not found");
            httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
            httpResponse.setSuccess(false);
            return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
        }

        Optional<Address> address = addressRepository.findById(id);
        if (address.isPresent()) {

            user_addressRepository.deleteByAddressId(id);
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setMessage("Address deleted");
            httpResponse.setStatus(HttpStatus.OK.value());
            httpResponse.setSuccess(true);

            addressRepository.deleteById(id);

            //Update customer primary address if it is deleted
            if(user_addresses.size() == 1){
                Customer customer = customerRepository.findById(authenticationFacade.getUserId()).get();
                customer.setPrimaryAddress(user_addresses.get(0).getAddressId());
                customerRepository.save(customer);
            }

            return ResponseEntity.ok(httpResponse);
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Address not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getAllAddresses(PageRequest pageRequest) {

        return ResponseEntity.ok(addressRepository.findAll(pageRequest));
    }

    public ResponseEntity<?> getAddressesByUserId(Integer userId) {

        Iterable<User_Address> user_addresses = user_addressRepository.findUser_AddressByUserId(userId);
        List<Address> addresses = new ArrayList<>();

        for (User_Address user_address : user_addresses) {
            Address address = addressRepository.findById(user_address.getAddressId()).get();
            addresses.add(address);
        }

        return ResponseEntity.ok(addresses);
    }

    public ResponseEntity<?> getAddressesByFirstName(String firstName, PageRequest pageRequest) {
        return ResponseEntity.ok(
         addressRepository.findAllByFirstname(firstName, pageRequest));
    }

    public ResponseEntity<?> getAddressesByLastName(String lastname, PageRequest pageRequest) {
        return ResponseEntity.ok(
                addressRepository.findAllByLastname(lastname, pageRequest));
    }

    public ResponseEntity<?> getAddressesByCity(String city, PageRequest pageRequest) {
        return ResponseEntity.ok(
                addressRepository.findByCity(city, pageRequest));
    }

    public ResponseEntity<?> getAddressesByState(String state, PageRequest pageRequest) {
        return ResponseEntity.ok(
                addressRepository.findByState(state, pageRequest));
    }

    public ResponseEntity<?> getAddressesByPostcode(String postcode, PageRequest pageRequest) {
        return ResponseEntity.ok(
                addressRepository.findByPostcode(postcode, pageRequest));
    }

    public ResponseEntity<?> makeAddressPrimary(Integer id) {


        Optional<Address> address = addressRepository.findById(id);
        if (address.isPresent()) {
            Customer customer = customerRepository.findById(authenticationFacade.getUserId()).get();

            System.out.println(customer.getPrimaryAddress());
            System.out.println(customer.getId());
            System.out.println(id);
            customer.setPrimaryAddress(id);

            Customer updatedCustomer = customerRepository.save(customer);
            System.out.println(updatedCustomer.getPrimaryAddress());
            return ResponseEntity.ok(updatedCustomer);
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Address not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return ResponseEntity.notFound().build();
    }
}
