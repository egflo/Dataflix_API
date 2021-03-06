package com.moviedb_api.customer;

import com.moviedb_api.HttpResponse;
import com.moviedb_api.address.Address;
import com.moviedb_api.address.AddressRepository;
import com.moviedb_api.sale.Sale;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional
public class CustomerService {

    @PersistenceContext
    EntityManager entityManager;


    @Autowired
    private AuthenticationFacade authenticationFacade;


    private final CustomerRepository customerRepository;
    private final User_AddressRepository user_addressRepository;
    private final AddressRepository addressRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public CustomerService( CustomerRepository customerRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                            User_AddressRepository user_addressRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.addressRepository = addressRepository;
        this.user_addressRepository = user_addressRepository;
    }



    public Integer updatePrimaryAddressId(Integer userId){

        Customer customer = customerRepository.findById(userId).get();
        List<User_Address> addresses = user_addressRepository.findByUserId(userId);

        if(!addresses.isEmpty() && customer.getPrimaryAddress() == 0) {
            User_Address pickedAddress = addresses.get(0);
            customer.setPrimaryAddress(pickedAddress.getAddressId());
            Customer c =  customerRepository.save(customer);
            return c.getPrimaryAddress();
        }

        else {
            return 0;
        }
    }

    public ResponseEntity<?> getCustomer(CustomerRequest request) {


        //If user not admin, check if userId is the same as the one in the request
        if(authenticationFacade.hasRole("USER")) {
            request.setId(authenticationFacade.getUserId());
        }

        else if(authenticationFacade.hasRole("ADMIN") && request.getId() == 0) {
           // return new ResponseEntity<>(HttpResponse.error("User id is required"), HttpStatus.BAD_REQUEST);
            request.setId(authenticationFacade.getUserId());
        }

        if (customerRepository.existsCustomerById(request.getId())) {
            return ResponseEntity.ok(customerRepository.findById(request.getId()).get());
        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Customer not found");
        response.setStatus(404);
        response.setSuccess(false);

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<?> updatePrimary(CustomerRequest request) {

        if(authenticationFacade.hasRole("USER")) {
            request.setId(authenticationFacade.getUserId());
        }

        Optional<Customer> presentCustomer = customerRepository.findById(request.getId());
        Optional<User_Address> presentAddress =
                user_addressRepository.findByUserIdAndAddressId(request.getId(), request.getPrimaryAddress());

        if(presentAddress.isPresent() && presentCustomer.isPresent()){
            Customer customer = presentCustomer.get();
            customer.setPrimaryAddress(request.getPrimaryAddress());

            HttpResponse response = new HttpResponse();
            response.setMessage("Customer Primary Address Updated");
            response.setStatus(200);
            response.setSuccess(true);
            response.data = customer;

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK);
        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Address not found");
        response.setStatus(404);
        response.setSuccess(false);

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<?> updateEmail(EmailRequest request) {

        if(authenticationFacade.hasRole("USER")) {
            request.setId(authenticationFacade.getUserId());
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(request.getId());

        if(optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            Boolean match = bCryptPasswordEncoder.matches(request.getPassword(), customer.getPassword());

            Boolean inUse = customerRepository.existsCustomerByEmail(request.getNewEmail());

           if(match && !inUse) {
               customer.setEmail(request.getNewEmail());
               customer = customerRepository.save(customer);

               HttpResponse response = new HttpResponse();
               response.setMessage("Email updated");
               response.setStatus(200);
               response.setSuccess(true);
               response.data = customer;

               return new ResponseEntity<>(
                       response, HttpStatus.OK);

           }

           else {
               HttpResponse response = new HttpResponse();
               response.setMessage("Unable to update email");
               response.setStatus(400);
               response.setSuccess(false);
               return ResponseEntity.badRequest().body(response);

           }
        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Customer not found");
        response.setStatus(404);
        response.setSuccess(false);

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> updatePassword(PasswordRequest request) {

        if(authenticationFacade.hasRole("USER")) {
            request.setId(authenticationFacade.getUserId());
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(request.getId());

        if(optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            Boolean match = bCryptPasswordEncoder.matches(request.getPassword(), customer.getPassword());

            if(match) {
                customer.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));

                HttpResponse response = new HttpResponse();
                response.setMessage("Password updated");
                response.setStatus(200);
                response.setSuccess(true);
                response.data = customer;

                return ResponseEntity.ok(response);
            }

            else {
                HttpResponse response = new HttpResponse();
                response.setMessage("Password is incorrect");
                response.setStatus(400);
                response.setSuccess(false);
                return ResponseEntity.badRequest().body(response);
            }
        }


        HttpResponse response = new HttpResponse();
        response.setMessage("Customer not found");
        response.setStatus(404);
        response.setSuccess(false);

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<?> addCustomer(CustomerRequest request){

        Customer customer = new Customer();
        customer.setFirstname(request.getFirstname());
        customer.setLastname(request.getLastname());
        customer.setEmail(request.getEmail());

        Optional<Customer> duplicate = customerRepository.findByEmail(request.getEmail());

        if(duplicate.isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        return ResponseEntity.ok(customerRepository.save(customer));
    }

    public ResponseEntity<?> updateCustomer(CustomerRequest request) {

        if(authenticationFacade.hasRole("USER")) {
            request.setId(authenticationFacade.getUserId());
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(request.getId());

        if(optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setFirstname(request.getFirstname());
            customer.setLastname(request.getLastname());
            customer.setEmail(request.getEmail());
            //customer.setPrimaryAddress(request.getPrimaryAddress() != null ? request.getPrimaryAddress() : customer.getPrimaryAddress());
            return ResponseEntity.ok(customerRepository.save(customer));
        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Customer not found");
        response.setStatus(404);
        response.setSuccess(false);

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> deleteCustomer(CustomerRequest request) {

        if(authenticationFacade.hasRole("USER")) {
            request.setId(authenticationFacade.getUserId());
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(request.getId());

        if(optionalCustomer.isPresent()) {
            HttpResponse response = new HttpResponse();
            response.setMessage("Customer deleted #" + request.getId());
            response.setStatus(200);
            response.setSuccess(true);

            customerRepository.deleteById(request.getId());
            return ResponseEntity.ok(response);
        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Customer not found");
        response.setStatus(404);
        response.setSuccess(false);

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> generateMetaData() {

        Calendar c= Calendar.getInstance();
        c.add(Calendar.DATE, -30);

        Date date = c.getTime();

        long total = customerRepository.count();
        long previous = customerRepository.findAllActiveUsersFromDate(date);
        double percent = ((total - previous) * 100) / total;

        // This returns a JSON or XML with the movies
        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("last_month", previous);
        response.put("change", percent);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK);
    }

    public ResponseEntity<?> findByFirstName(String fname, Pageable pageable) {
        return ResponseEntity.ok(customerRepository.findCustomerByFirstname(fname, pageable));
    }

    public ResponseEntity<?> findByLastName(String fname, Pageable pageable) {
        return ResponseEntity.ok(customerRepository.findCustomerByLastname(fname, pageable));
    }

    public ResponseEntity<?> findByEmail(String email) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);

        if(optionalCustomer.isPresent()) {
            return ResponseEntity.ok(optionalCustomer.get());
        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Customer with Email not found");
        response.setStatus(404);
        response.setSuccess(false);


        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getAll(Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(pageable);
        return new ResponseEntity<>(
                customers,
                HttpStatus.OK);
    }
    public ResponseEntity<?> search(String search, PageRequest pageable) {

        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        boolean isNumber = pattern.matcher(search).matches();
        Page<Sale> sales = null;

        if(isNumber) {
            // This returns a JSON or XML with the movies
            return ResponseEntity.ok(customerRepository.findByCustomerId(Integer.parseInt(search), pageable));

        }
        else {
            // This returns a JSON or XML with the movies
            return ResponseEntity.ok(customerRepository.findCustomerByFirstnameContainingOrLastnameContainingOrEmailContaining(
                    search,
                    search,
                    search,
                    pageable));

        }
    }

}