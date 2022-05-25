package com.moviedb_api.security;

import com.moviedb_api.API;
import com.moviedb_api.HttpResponse;
import com.moviedb_api.cart.Cart;
import com.moviedb_api.customer.Customer;
import com.moviedb_api.customer.CustomerRepository;
import com.moviedb_api.refreshToken.RefreshToken;
import com.moviedb_api.refreshToken.RefreshTokenService;
import com.moviedb_api.refreshToken.TokenRefreshException;
import com.moviedb_api.roles.RoleService;
import com.moviedb_api.roles.Roles;
import com.moviedb_api.security.TokenRefreshRequest;
import com.moviedb_api.security.TokenRefreshResponse;
import org.json.HTTP;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleService roleService;



    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader HttpHeaders headers) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        Map<String, Object> response = new HashMap<>();

        if(jwtTokenUtil.validate(token)) {
            response.put("message","Access to content is authorized.");
            response.put("status",200);
            return new ResponseEntity<>(
                    HttpStatus.OK);
        }
        else {
            response.put("message","Access to content is unauthorized.");
            response.put("status",401);
            return new ResponseEntity<>(
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody String authenticationRequest) throws Exception {
        JSONObject request = new JSONObject(authenticationRequest);
        String password = request.getString("password");
        String email = request.getString("email");
        String firstName = request.getString("firstname");
        String lastName = request.getString("lastname");

        Optional<Customer> present = customerRepository.findByEmail(email);
        if(present.isPresent()) {
            HttpResponse response = new HttpResponse();
            response.setMessage("User already exists.");
            response.setStatus(400);
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setFirstname(firstName);
        customer.setLastname(lastName);
        customer.setPassword(new BCryptPasswordEncoder().encode(password));


        Customer save = customerRepository.save(customer);
        roleService.insertWithQuery(save.getId(), Roles.USER);

        if(save == null) {
            HttpResponse response = new HttpResponse();
            response.setMessage("User could not be created.");
            response.setStatus(400);
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }


        final Customer userDetails = userDetailsService
                .loadUserByUsername(customer.getEmail());

        final String token = jwtTokenUtil.generateAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customer.getId());


        UserResponse userResponse = new UserResponse();
        userResponse.setId(customer.getId());
        userResponse.setUsername(customer.getEmail());
        userResponse.setAccessToken(token);
        userResponse.setRoles(userDetails.getAuthorities());
        userResponse.setRefreshToken(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(userResponse);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(@RequestHeader HttpHeaders headers) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();

        Integer userId = Integer.valueOf(jwtTokenUtil.getUserId(token));
        refreshTokenService.deleteByUserId(userId);

        HttpResponse response = new HttpResponse();
        response.setMessage("User logged out successfully.");
        response.setStatus(200);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody String authenticationRequest) throws Exception {

        JSONObject request = new JSONObject(authenticationRequest);
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //String encodedPassword = passwordEncoder.encode(request.getString("password"));

        Authentication authenticate = authenticate(request.getString("username"), request.getString("password"));

        final Customer userDetails = userDetailsService
                .loadUserByUsername(request.getString("username"));

        final String token = jwtTokenUtil.generateAccessToken(userDetails);

        Customer customer = (Customer) authenticate.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customer.getId());

        UserResponse userResponse = new UserResponse();
        userResponse.setId(customer.getId());
        userResponse.setUsername(customer.getEmail());
        userResponse.setAccessToken(token);
        userResponse.setRoles(userDetails.getAuthorities());
        userResponse.setRefreshToken(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(userResponse);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) throws Exception {

        System.out.println("Generating new token");

        Optional<RefreshToken> refreshTokenOptional = refreshTokenService.findByToken(request.getRefreshToken());

        if(!refreshTokenOptional.isPresent()) {

            //Return unauthorized if token is invalid
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        RefreshToken refreshToken = refreshTokenOptional.get();
        refreshTokenService.verifyExpiration(refreshToken);

        Customer user = refreshToken.getUser();
        String token = jwtTokenUtil.generateAccessToken(user);

        TokenRefreshResponse response = new TokenRefreshResponse(token, refreshToken.getToken());


        return ResponseEntity.ok(response);

    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            //System.out.println(username + " " + password);
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return authenticate;

        } catch (DisabledException e) {
            System.out.println("USER_DISABLED");
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            System.out.println("INVALID_CREDENTIALS");
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            throw new Exception("GENERAL_EXCEPTION",e);
        }
    }
}