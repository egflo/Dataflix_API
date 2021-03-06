package com.moviedb_api.genres_in_movies;
import com.moviedb_api.genre.Genre;
import com.moviedb_api.movie.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller // This means that this class is a Controller
@RequestMapping(path="/genre_movie") // This means URL's start with /demo (after Application path
public class Genre_MovieController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private Genre_MovieRepository genre_movieRepository;

    @GetMapping(path="/all")
    public @ResponseBody
    Page<Genre> getAllGenre_Movie(
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {
        // This returns a JSON or XML with the movies
        return genre_movieRepository.getAllGenres(
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/name/{name}")
    public @ResponseBody
    Page<Genre_Movie> getGenreById(
            @PathVariable(value = "name") String name,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy)
    {
        return genre_movieRepository.findByGenreName(
                name,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }

    @GetMapping("/{id}")
    public @ResponseBody
    Page<Genre_Movie> getGenreById(
            @PathVariable(value = "id") Integer id,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy)
    {
        return genre_movieRepository.findByGenreId(
                id,
                PageRequest.of(
                        page.orElse(0),
                        limit.orElse(5),
                        Sort.Direction.ASC, sortBy.orElse("id")
                )
        );
    }
   // Iterable<Genre_Movie> getGenreById(@PathVariable(value = "id") Integer id)
   // {
   //     return genre_movieRepository.findByGenreId(id);
   // }
}