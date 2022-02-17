package com.moviedb_api.security;


import com.moviedb_api.customer.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private final CustomerRepository userRepo;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(CustomerRepository userRepo,
                          JwtTokenFilter jwtTokenFilter) {
        this.userRepo = userRepo;
        this.jwtTokenFilter = jwtTokenFilter;
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
                .antMatchers("/user/**").permitAll()

                .antMatchers("/customer/").hasAnyAuthority("USER","ADMIN")
                .antMatchers("/customer/update/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers("/customer/**").hasAnyAuthority("ADMIN")

                .antMatchers("/bookmark/").hasAnyAuthority("USER","ADMIN")
                .antMatchers("/bookmark/{id}").hasAnyAuthority("USER","ADMIN")
                //.antMatchers("/bookmark/**").hasAnyAuthority("ADMIN")

                .antMatchers( "/cart/").hasAnyAuthority("USER","ADMIN")
                .antMatchers("/cart/qty/").hasAnyAuthority("USER","ADMIN")
                //.antMatchers("/cart/**").hasAnyAuthority("ADMIN")

                .antMatchers("/customer/update/**").hasAnyAuthority("USER","ADMIN")
                //.antMatchers("/customer/**").hasAnyAuthority("ADMIN")

                .antMatchers("/tax/**").hasAnyAuthority("ADMIN")

                .antMatchers("/sale/").hasAnyAuthority("USER","ADMIN")
                //.antMatchers("/sale/{id}").hasAnyAuthority("USER","ADMIN")
                .antMatchers("/sale/**").hasAnyAuthority("USER","ADMIN")

                .antMatchers(HttpMethod.GET,"/review/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/ratings/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT,"/ratings/**").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.GET,"/ratings/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/ratings/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT,"/ratings/**").hasAnyAuthority("ADMIN")

                .antMatchers("/price/**").hasAnyAuthority("ADMIN")



                .antMatchers(HttpMethod.GET,"/genre/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/genre/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT,"/genre/**").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.GET,"/movie/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST,"/movie/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT,"/movie/**").hasAnyAuthority("ADMIN")

                .antMatchers("/tax/**").hasAnyAuthority("ADMIN")

                .antMatchers("/order/sellers").hasAnyAuthority("USER","ADMIN")
                .antMatchers("/order/**").hasAnyAuthority("ADMIN")


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

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        //config.setAllowedOrigins(List.of("http://localhost:3000","https://*.vercel.app"));
        config.setAllowedOriginPatterns(List.of("http://localhost:3000","http://localhost:3001","https://*.vercel.app"));
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

