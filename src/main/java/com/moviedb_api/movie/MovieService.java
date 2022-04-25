package com.moviedb_api.movie;

import com.moviedb_api.genres_in_movies.Genre_Movie;
import com.moviedb_api.genres_in_movies.Genre_MovieRepository;
import com.moviedb_api.inventory.InventoryRepository;
import com.moviedb_api.price.Price;
import com.moviedb_api.price.PriceRepository;
import com.moviedb_api.ratings.Rating;
import com.moviedb_api.ratings.RatingRepository;
import com.moviedb_api.cast.Star_Movie;
import com.moviedb_api.cast.Star_MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {

    @Autowired
    EntityManager entityManager;

    private final MovieRepository movieRepository;
    private final Genre_MovieRepository genreRepository;
    private final Star_MovieRepository castRepository;
    private final RatingRepository ratingRepository;
    private final PriceRepository priceRepository;
    private final InventoryRepository inventoryRepository;

    public MovieService(MovieRepository movieRepository,
                        Star_MovieRepository castRepository,
                        Genre_MovieRepository genreRepository,
                        RatingRepository ratingRepository,
                        PriceRepository priceRepository,
                        InventoryRepository inventoryRepository) {
        this.movieRepository = movieRepository;
        this.castRepository = castRepository;
        this.genreRepository = genreRepository;
        this.ratingRepository = ratingRepository;
        this.priceRepository = priceRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public String checkifnullorempty(String s){
        if(s == null || s.isEmpty()){
            return "";
        }
        return s;
    }

    public String formatStatus(String score){

        if(score == null || score.isEmpty()){
            return "";
        }

        Double scoreDouble = Double.parseDouble(score);
        if(scoreDouble >= 60){
            return "Fresh";
        }
        else if(scoreDouble < 60 && scoreDouble >= 1){
            return "Rotten";
        }

        return "";
    }

    public String formatAudienceStatus(String score){

        if(score == null || score.isEmpty()){
            return "";
        }

        Double scoreDouble = Double.parseDouble(score);
        if(scoreDouble >= 60){
            return "Upright";
        }
        else if(scoreDouble < 60 && scoreDouble >= 1){
            return "Spilled";
        }

        return "";

    }

    public Movie createMovie(MovieRequest request) {

        Movie movie = new Movie();
        movie.setId(request.getId());
        movie.setTitle(checkifnullorempty(request.getTitle()));
        movie.setDirector(checkifnullorempty(request.getDirector()));
        movie.setPlot(checkifnullorempty(request.getPlot()));
        movie.setRated(checkifnullorempty(request.getRated()));
        movie.setRuntime(checkifnullorempty(request.getRuntime()));
        movie.setLanguage(checkifnullorempty(request.getLanguage()));
        movie.setCountry(checkifnullorempty(request.getCountry()));
        movie.setAwards(checkifnullorempty(request.getAwards()));
        movie.setPoster(checkifnullorempty(request.getPoster()));
        movie.setBoxOffice(checkifnullorempty(request.getBoxOffice()));
        movie.setProduction(checkifnullorempty(request.getProduction()));
        movie.setWriter(checkifnullorempty(request.getWriter()));
        movie.setBackground(checkifnullorempty(request.getBackground()));
        movie.setUpdated(new java.sql.Date(System.currentTimeMillis()));
        movie.setCached(0);

        Movie m = movieRepository.save(movie);

        Price price = new Price();
        price.setMovieId(m.getId());
        price.setPrice(request.getPrice());

        priceRepository.save(price);


        if (request.getRatings() != null) {

            RatingRequest ratingRequest = request.getRatings();

            //System.out.println(ratingRequest.getIMDB());
            //System.out.println(ratingRequest.getMetacritic());
            //System.out.println(ratingRequest.getRottenTomatoes());
            //System.out.println(ratingRequest.getRottenTomatoesAudience());

            Rating rating = new Rating();
            rating.setNumVotes(0);
            rating.setRating(0);

            rating.setMovieId(m.getId());
            rating.setImdb(ratingRequest.getIMDB());
            rating.setMetacritic(ratingRequest.getMetacritic());
            rating.setRottenTomatoes(ratingRequest.getRottenTomatoes());
            rating.setRottenTomatoesAudience(ratingRequest.getRottenTomatoesAudience());

            rating.setRottenTomatoesStatus(formatStatus(ratingRequest.getRottenTomatoes()));
            rating.setRottenTomatoesAudienceStatus(formatAudienceStatus(ratingRequest.getRottenTomatoesAudience()));

            ratingRepository.save(rating);
        }


        for(GenreRequest genre : request.getGenres()){
            Genre_Movie genre_movie = new Genre_Movie();
            genre_movie.setMovieId(m.getId());
            genre_movie.setGenreId(genre.getId());

            genreRepository.save(genre_movie);
        }

        for(CastRequest cast : request.getCasts()){
            Star_Movie star_movie = new Star_Movie();
            star_movie.setMovieId(m.getId());
            star_movie.setStarId(cast.getStarId());
            star_movie.setCharacters(cast.getCharacters());
            star_movie.setCategory(cast.getCategory());

            castRepository.save(star_movie);
        }

        return m;

    }

    public Boolean updateMovie(MovieRequest request) {
        Optional<Movie> update = movieRepository.findById(request.getId());
        if (update.isPresent()) {
            Movie movie = update.get();
            movie.setTitle(checkifnullorempty(request.getTitle()));
            movie.setDirector(checkifnullorempty(request.getDirector()));
            movie.setPlot(checkifnullorempty(request.getPlot()));
            movie.setRated(checkifnullorempty(request.getRated()));
            movie.setRuntime(checkifnullorempty(request.getRuntime()));
            movie.setLanguage(checkifnullorempty(request.getLanguage()));
            movie.setCountry(checkifnullorempty(request.getCountry()));
            movie.setAwards(checkifnullorempty(request.getAwards()));
            movie.setPoster(checkifnullorempty(request.getPoster()));
            movie.setBoxOffice(checkifnullorempty(request.getBoxOffice()));
            movie.setProduction(checkifnullorempty(request.getProduction()));
            movie.setWriter(checkifnullorempty(request.getWriter()));
            movie.setBackground(checkifnullorempty(request.getBackground()));
            movie.setUpdated(new java.sql.Date(System.currentTimeMillis()));

            Price price = new Price();
            price.setMovieId(request.getId());
            price.setPrice(request.getPrice());

            priceRepository.save(price);


            System.out.println("Ratings updated");
            RatingRequest ratingRequest = request.getRatings();
            Optional<Rating> rating = ratingRepository.findByMovieId(request.getId());
            if (ratingRequest != null && rating.isPresent()) {

                //System.out.println(ratingRequest.getIMDB());
                //System.out.println(ratingRequest.getMetacritic());
                //System.out.println(ratingRequest.getRottenTomatoes());
                //System.out.println(ratingRequest.getRottenTomatoesAudience());

                Rating updateRating = rating.get();
                updateRating.setImdb(ratingRequest.getIMDB());
                updateRating.setMetacritic(ratingRequest.getMetacritic());
                updateRating.setRottenTomatoes(ratingRequest.getRottenTomatoes());
                updateRating.setRottenTomatoesAudience(ratingRequest.getRottenTomatoesAudience());

                ratingRepository.save(updateRating);
            }

            System.out.println("Genre updated");
            List<Genre_Movie> old_genres = genreRepository.findByMovieId(movie.getId());
            List<GenreRequest> new_genres = request.getGenres();

            for (GenreRequest genre : new_genres) {
                Genre_Movie new_genre = new Genre_Movie();
                new_genre.setMovieId(movie.getId());
                new_genre.setGenreId(genre.getId());
                genreRepository.save(new_genre);
            }

            for (Genre_Movie g : old_genres) {
                genreRepository.deleteById(g.getId());
            }

            System.out.println("Cast updated");
            List<Star_Movie> old_casts = castRepository.findByMovieId(movie.getId());
            List<CastRequest> new_casts = request.getCasts();

            for (CastRequest castRequest : new_casts) {
                //System.out.println(castRequest.getStarId());
                //System.out.println(castRequest.getCharacters());
                //System.out.println(castRequest.getCategory());

                Star_Movie new_cast = new Star_Movie();
                new_cast.setMovieId(movie.getId());
                new_cast.setStarId(castRequest.getStarId());
                new_cast.setCharacters(castRequest.getCharacters());
                new_cast.setCategory(castRequest.getCategory());
                castRepository.save(new_cast);
            }

            for (Star_Movie s : old_casts) {
                castRepository.deleteById(s.getId());
           }

            //return movieRepository.findById(request.getId()).get();
            //entityManager.flush();
            //Movie updated = movieRepository.findById(request.getId()).get();

            movieRepository.save(movie);

            return true;

        }
        return false;
    }

}