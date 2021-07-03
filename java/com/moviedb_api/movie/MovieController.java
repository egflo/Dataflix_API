package com.moviedb_api.movie;



import com.fasterxml.jackson.annotation.JsonView;
import com.moviedb_api.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller // This means that this class is a Controller
@RequestMapping(path="/movie") // This means URL's start with /movies (after Application path)
public class MovieController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private MovieRepository movieRepository;

    @GetMapping(path="/all")
    @JsonView(Views.Public.class)
    public @ResponseBody
    Page<Movie> getAllMovies(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<String> sortBy
    ) {
        // This returns a JSON or XML with the movies
        return movieRepository.findAll(
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping(path="/title/{title}")
    @JsonView(Views.Public.class)
    public @ResponseBody
    Iterable<Movie> findMovieByTitle(
            @PathVariable String title,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    )
    {
        System.out.println(title);
        // This returns a JSON or XML with the movies
        //return movieRepository.findByTitle(title);
        return movieRepository.findByTitleContaining(
                title,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping(path="/year/{year}")
    @JsonView(Views.Public.class)
    public @ResponseBody
    Page<Movie> findMoviesByYear(
            @PathVariable Integer year,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {
        // This returns a JSON or XML with the movies
        //return movieRepository.findByYear(year);
        return movieRepository.findByYear(
                year,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping(path="/rated/{rated}")
    @JsonView(Views.Public.class)
    public @ResponseBody
    Page<Movie> findMoviesByRating(
            @PathVariable String rated,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy) {
        // This returns a JSON or XML with the movies
        //return movieRepository.findAllByRated(rated);
        return movieRepository.findByRated(
                rated,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public @ResponseBody
    Optional<Movie> findMovieById(
            @PathVariable(value = "id") String id)
    {
        return movieRepository.findById(id);
    }

    @GetMapping("/genre/name/{name}")
    @JsonView(Views.Public.class)
    public @ResponseBody
    Page<Movie> findMoviesByGenre(
            @PathVariable(value = "name") String name,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy)

    {
        return movieRepository.findMovieByGenreName(
                name,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/genre/{id}")
    @JsonView(Views.Public.class)
    public @ResponseBody
    Page<Movie> findMoviesByGenreId(
            @PathVariable(value = "id") Integer id,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy)

    {
        return movieRepository.findMovieByGenreId(
                id,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/star/{id}")
    @JsonView(Views.Public.class)
    public @ResponseBody
    Page<Movie> findMoviesByStarId(
            @PathVariable(value = "id") String id,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy)

    {
        return movieRepository.findMovieByStarId(
                id,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }
}