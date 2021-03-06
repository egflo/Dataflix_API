package com.moviedb_api.ratings;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.moviedb_api.Views;
import com.moviedb_api.movie.MovieRepository;
import com.moviedb_api.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@Controller // This means that this class is a Controller
@RequestMapping(path="/rating") // This means URL's start with /movies (after Application path)
public class RatingController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private RatingRepository ratingRepository;

    @Autowired // This means to get the bean called userRepository
    private MovieRepository movieRepository;

    //@JsonView({Views.Rating.class})
    @GetMapping(path="/all")
    public @ResponseBody
    ResponseEntity<?> getAllRatings(
        @RequestParam Optional<Integer> limit,
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<String> sortBy) throws JsonProcessingException {

        SimpleBeanPropertyFilter movieFilter =
                SimpleBeanPropertyFilter.filterOutAllExcept("id", "title", "background", "poster", "year", "runtime", "rated");
        SimpleBeanPropertyFilter ratingFilter =
                SimpleBeanPropertyFilter.filterOutAllExcept("movieId", "rating", "numVotes", "movie", "imdb","metacritic", "rottenTomatoes", "rottenTomatoesAudience", "rottenTomatoesStatus", "rottenTomatoesAudienceStatus");

        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter("movieFilter", movieFilter).addFilter("ratingFilter", ratingFilter);

        Page<Rating> rating = ratingRepository.findAll(
                    PageRequest.of(
                            page.orElse(0),
                            limit.orElse(5),
                            Sort.Direction.ASC, sortBy.orElse("movieId")
                    )
            );


        MappingJacksonValue response = new MappingJacksonValue(rating);
        response.setFilters(filters);

        //FilterProvider filterProvider = new SimpleFilterProvider()
         //       .addFilter("movieFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "title", "year", "rating"));
         //   ObjectMapper mapper = new ObjectMapper();
          //  mapper.setFilterProvider(filterProvider);
          //  String json = mapper.writeValueAsString(rating);

            return ResponseEntity.ok(rating);
    }

    @GetMapping(path="/rated")
    public @ResponseBody
    Page<Rating> getTopRated(
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page
    ){



        Page<Rating> ratings = ratingRepository.findBestRated(
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5)
                )
        );

        return ratings;
    }

    @GetMapping(path="/critic")
    public @ResponseBody
    Page<Rating> getCriticAcc(
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page
    ){
        return ratingRepository.findBestCritic(
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5)
                )
        );
    }

    @GetMapping("/{id}")
    public @ResponseBody
    Optional<Rating> findOrderById(@PathVariable(value = "id") String id)
    {
        return ratingRepository.findByMovieId(id);
    }
}