package com.moviedb_api.security;


import com.moviedb_api.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

//https://www.baeldung.com/spring-security-authentication-with-a-database
//https://www.toptal.com/spring/spring-security-tutorial

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    private final CustomerRepository userRepo;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(CustomerRepository userRepo,
                          JwtTokenFilter jwtTokenFilter) {
        this.userRepo = userRepo;
        this.jwtTokenFilter = jwtTokenFilter;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();


        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();


        // Set permissions on endpoints
        http.authorizeRequests()


                //Address endpoints
                .antMatchers(HttpMethod.GET, "/address/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/address/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/address/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE, "/address/**").hasAnyAuthority("USER","ADMIN")

                //Bookmark endpoints
                .antMatchers(HttpMethod.GET, "/bookmark/{id}").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.GET, "/bookmark/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/bookmark/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/bookmark/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE, "/bookmark/{id}").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE, "/bookmark/").hasAnyAuthority("USER","ADMIN")

                .antMatchers(HttpMethod.GET, "/bookmark/all").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/bookmark/movie/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/bookmark/customer/{id}").hasAnyAuthority("ADMIN")

                //Cart endpoints
                .antMatchers(HttpMethod.GET, "/cart/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/cart/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/cart/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE, "/cart/{id}").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.GET, "/cart/qty/").hasAnyAuthority("USER","ADMIN")

                .antMatchers(HttpMethod.GET, "/cart/all").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/cart/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/cart/movie/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/cart/customer/{id}").hasAnyAuthority("ADMIN")

                //Customer endpoints
                .antMatchers(HttpMethod.GET, "/customer/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/customer/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/customer/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE, "/customer/**").hasAnyAuthority("USER","ADMIN")

                //Order endpoints
                .antMatchers(HttpMethod.GET, "/order/**").hasAnyAuthority("ADMIN")

                //Cast endpoints
                .antMatchers(HttpMethod.GET, "/cast/**").hasAnyAuthority("ADMIN","USER")
                .antMatchers(HttpMethod.POST, "/cast/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/cast/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/cast/**").hasAnyAuthority("ADMIN")

                //Checkout endpoints
                .antMatchers(HttpMethod.GET, "/checkout/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/checkout/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/checkout/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE, "/checkout/**").hasAnyAuthority("USER","ADMIN")

                //Customer endpoints
                .antMatchers(HttpMethod.GET, "/customer/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.GET, "/customer/{id}").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/customer/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/customer/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/customer/primary").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/customer/email").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/customer/password").hasAnyAuthority("USER","ADMIN")

                .antMatchers(HttpMethod.GET, "/customer/search/{search}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/customer/metadata").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/customer/all").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/customer/firstname/{fname}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/customer/lastname/{lname}").hasAnyAuthority("ADMIN")


                //Employee endpoints
                .antMatchers(HttpMethod.GET, "/employee/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/employee/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/employee/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/employee/**").hasAnyAuthority("ADMIN")

                //Genre endpoints
                .antMatchers(HttpMethod.GET, "/genre/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/genre/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/genre/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/genre/**").hasAnyAuthority("ADMIN")

                //Genre Movie endpoints
                .antMatchers(HttpMethod.GET, "/genre_movie/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/genre_movie/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/genre_movie/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/genre_movie/**").hasAnyAuthority("ADMIN")

                //Inventory endpoints
                .antMatchers(HttpMethod.GET, "/inventory/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/inventory/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/inventory/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/inventory/**").hasAnyAuthority("ADMIN")


                //Movie endpoints
                .antMatchers(HttpMethod.GET, "/movie/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/movie/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/movie/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/movie/**").hasAnyAuthority("ADMIN")

                //Order endpoints
                .antMatchers(HttpMethod.GET, "/order/**").hasAnyAuthority("ADMIN","USER")
                .antMatchers(HttpMethod.POST, "/order/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/order/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/order/**").hasAnyAuthority("ADMIN")

                //Price endpoints
                .antMatchers(HttpMethod.GET, "/price/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/price/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/price/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/price/**").hasAnyAuthority("ADMIN")

                //Rating endpoints
                .antMatchers(HttpMethod.GET, "/rating/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/rating/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/rating/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/rating/**").hasAnyAuthority("ADMIN")

                //Review endpoints
                .antMatchers(HttpMethod.GET, "/review/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/review/}").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/review/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/review/{id}").hasAnyAuthority("ADMIN")


                //Sales endpoints
                .antMatchers(HttpMethod.GET, "/sale/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.GET, "/sale/{id}").hasAnyAuthority("USER","ADMIN")

                .antMatchers(HttpMethod.POST, "/sale/").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.PUT, "/sale/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/sale/").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/sales/**").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/sale/search/{search}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/sale/customer/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/sale/metadata/").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/sale/metadata/").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/sale/all").hasAnyAuthority("ADMIN")


                //Shipping endpoints
                .antMatchers(HttpMethod.GET, "/shipping/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/shipping/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/shipping/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/shipping/**").hasAnyAuthority("ADMIN")

                //Tax endpoints
                .antMatchers(HttpMethod.GET, "/tax/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/tax/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/tax/**").hasAnyAuthority("ADMIN")

                //User_Address endpoints
                .antMatchers(HttpMethod.GET, "/user_address/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/user_address/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/user_address/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/user_address/**").hasAnyAuthority("ADMIN")


                // Allow all requests to the following endpoints without authentication
                .antMatchers("/user/**").permitAll()



                //.antMatchers(HttpMethod.GET,"/star/**").permitAll()
                //.antMatchers(HttpMethod.GET,"/review/**").permitAll()
                //.antMatchers(HttpMethod.GET,"/customer/**").permitAll()
               // .antMatchers(HttpMethod.GET,"/sale/**").permitAll()
                //.antMatchers(HttpMethod.GET,"/cart/**").permitAll()
               // .antMatchers("/order/**").permitAll()
                //.antMatchers("/rating/**").permitAll()
                // Our private endpoints
                .anyRequest().authenticated();

        // Add JWT token filter
        http.addFilterBefore(
                jwtTokenFilter,
                UsernamePasswordAuthenticationFilter.class
        );

    }

    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }


    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        //config.setAllowedOrigins(List.of("http://localhost:3000","https://*.vercel.app"));
        config.setAllowedOriginPatterns(List.of("http://localhost:3000","http://localhost:3001", "https://47.156.71.215", "http://47.156.71.215", "https://*.vercel.app"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

