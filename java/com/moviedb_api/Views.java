package com.moviedb_api;


import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "/**")
public interface Views {
     public static interface Public {

    }

    public static interface Summary extends Public {

    }

}
