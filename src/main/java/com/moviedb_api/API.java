package com.moviedb_api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moviedb_api.movie.Movie;
import com.moviedb_api.movie.MovieRepository;
import com.moviedb_api.ratings.Rating;
import com.moviedb_api.ratings.RatingRepository;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

class Customer
{
    public Integer id;
    public String password;
};

public class API {
    @Value("${moviedb.api.key}")
    String API_KEY;

    @Value("${background_api.key}")
    String API_KEY_BACKGROUND;

    public int background(String movie_id)  {


        int row_ratings, row_movie = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String USER = "root";
            String PASSWORD = "12251225";

            // Connect to the test database
            Connection connection = DriverManager.getConnection
                    ("jdbc:mysql://10.81.1.123:4406/moviedb_api?useSSL=false",USER, PASSWORD);


            String movie_query = "SELECT background FROM movies WHERE id = ?";
            PreparedStatement count_stmt = connection.prepareStatement(movie_query);

            count_stmt.setString(1,movie_id);
            ResultSet result = count_stmt.executeQuery();

            String movie_background = "";

            //Movie Exists
            if(result.next()) {
                //Is movie cached in database? 1:Yes, 0:No
                movie_background = result.getString("background");
            }

            else {
                return 0;
            }

            if(movie_background != null) {
                return 0;
            }

            if(movie_background.length() >= 4) {
                return 0;
            }

            // Create a neat value object to hold the URL
            URL url = new URL("http://webservice.fanart.tv/v3/movies/" + movie_id + "?api_key=" + API_KEY);

            System.out.println(url.toString());

            // Open a connection(?) on the URL(??) and cast the response(???)
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Now it's "open", we can set the request method, headers etc.
            conn.setRequestProperty("accept", "application/json");

            // This line makes the request
            InputStream inputStream = conn.getInputStream();

            //Convert to String JSON
            String content = IOUtils.toString(inputStream);

            //System.out.println(content);

            JsonObject json = new JsonParser().parse(content).getAsJsonObject();
            JsonArray data = json.get("moviebackground").getAsJsonArray();

            if(data.size() != 0) {
                JsonObject object = data.get(0).getAsJsonObject();

                JsonElement background_url = object.get("url");


                System.out.println("URL " + background_url.getAsString());


                String query = "UPDATE movies SET background=? WHERE id=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1,background_url.getAsString());
                statement.setString(2,movie_id);

                row_movie = statement.executeUpdate();

            }

            else {
                return 0;
            }

            //connection.commit();

        }
        catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            //System.out.println(exceptionAsString);
            System.out.println("ERROR MOVIE ID " + movie_id);

        }

        return row_movie;
    }




    public int ProcessMovie(String movie_id, MovieRepository movieRepository, RatingRepository ratingRepository) {
        try {
            System.out.println("Processing Movie ID " + movie_id);
            Optional<Movie> present = movieRepository.findById(movie_id);
            if(present.isPresent()) {
                Movie movie = present.get();
                Integer cached = movie.getCached();
                if(cached == 1 || cached == 0) {
                    //Already cached
                    return 0;
                }


                // Create a neat value object to hold the URL
                URL url = new URL("http://www.omdbapi.com/?i="+movie_id+"&apikey="+API_KEY);

                // Open a connection(?) on the URL(??) and cast the response(???)
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Now it's "open", we can set the request method, headers etc.
                conn.setRequestProperty("accept", "application/json");

                // This line makes the request
                InputStream inputStream = conn.getInputStream();

                //Convert to String JSON
                String content = IOUtils.toString(inputStream);

                JsonObject json = new JsonParser().parse(content).getAsJsonObject();
                String title = json.get("Title").getAsString();
                String year = json.get("Year").getAsString();
                String rated = json.get("Rated").getAsString();
                String runtime = json.get("Runtime").getAsString();
                String director = json.get("Director").getAsString();
                String plot = json.get("Plot").getAsString();
                String language = json.get("Language").getAsString();
                String poster = json.get("Poster").getAsString();
                String box_office = json.get("BoxOffice").getAsString();
                String production = json.get("Production").getAsString();
                String country = json.get("Country").getAsString();
                String awards = json.get("Awards").getAsString();
                String writer = json.get("Writer").getAsString();


                JsonArray ratings = json.get("Ratings").getAsJsonArray();
                HashMap<String,String> movie_ratings = new HashMap<>();
                for(int i = 0; i < ratings.size(); i++) {
                    JsonElement source = ratings.get(i).getAsJsonObject().get("Source");
                    JsonElement value = ratings.get(i).getAsJsonObject().get("Value");

                    movie_ratings.put(source.getAsString(),value.getAsString());
                }

                String imdb = movie_ratings.get("Internet Movie Database");
                String rotten_tom = movie_ratings.get("Rotten Tomatoes");
                String metacritic = movie_ratings.get("Metacritic");

                Optional<Rating> present_rating = ratingRepository.findById(movie_id);
                if(present_rating.isPresent()) {
                    Rating rating = present_rating.get();
                    rating.setImdb(rating.getImdb() == null || rating.getImdb().strip().length() == 0 ? imdb : rating.getImdb());
                    rating.setRottenTomatoes(rating.getRottenTomatoes() == null || rating.getRottenTomatoes().strip().length() == 0 ? rotten_tom : rating.getRottenTomatoes());
                    rating.setMetacritic(rating.getMetacritic() == null || rating.getMetacritic().strip().length() == 0 ? metacritic : rating.getMetacritic());

                    ratingRepository.save(rating);
                }

                movie.setTitle(title);

                if(movie.getPlot() == null || movie.getPlot().strip().length() == 0) {
                    movie.setPlot(plot);
                }

                if(movie.getRated() == null || movie.getRated().strip().length() == 0) {
                    movie.setRated(rated);
                }

                if(movie.getRuntime() == null || movie.getRuntime().strip().length() == 0) {
                    movie.setRuntime(runtime);
                }

                if(movie.getLanguage() == null || movie.getLanguage().strip().length() == 0) {
                    movie.setLanguage(language);
                }

                if(movie.getPoster() == null || movie.getPoster().strip().length() == 0) {
                    movie.setPoster(poster);
                }

                if(movie.getWriter() == null || movie.getWriter().strip().length() == 0) {
                    movie.setWriter(writer);
                }

                if(movie.getAwards() == null || movie.getAwards().strip().length() == 0) {
                    movie.setAwards(awards);
                }

                if(movie.getBoxOffice() == null || movie.getBoxOffice().strip().length() == 0) {
                    movie.setBoxOffice(box_office);
                }

                if(movie.getProduction() == null || movie.getProduction().strip().length() == 0) {
                    movie.setProduction(production);
                }

                if(movie.getCountry() == null || movie.getCountry().strip().length() == 0) {
                    movie.setCountry(country);
                }

                movieRepository.save(movie);

                try {

                    // Create a neat value object to hold the URL
                    url = new URL("http://webservice.fanart.tv/v3/movies/" + movie_id + "?api_key=" + API_KEY_BACKGROUND);


                    // Open a connection(?) on the URL(??) and cast the response(???)
                    conn = (HttpURLConnection) url.openConnection();

                    // Now it's "open", we can set the request method, headers etc.
                    conn.setRequestProperty("accept", "application/json");

                    // This line makes the request
                    inputStream = conn.getInputStream();

                    //Convert to String JSON
                    content = IOUtils.toString(inputStream);

                    //System.out.println(content);

                    json = new JsonParser().parse(content).getAsJsonObject();

                    JsonArray data = json.get("moviebackground").getAsJsonObject().get("moviebackground").getAsJsonArray();

                    if(data.size() != 0) {
                        JsonObject object = data.get(0).getAsJsonObject();

                        JsonElement background_url = object.get("url");


                        movie.setBackground(background_url.getAsString());
                        movieRepository.save(movie);

                    }

                    else {
                        return 0;
                    }

                } catch (Exception e) {
                    System.out.println("No background art found");

                }

            }

            else {
                //Movie is not cached
                return 0;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return 1;
        }
    }

    /**
    public static int movie(String movie_id)  {
        String API_KEY = "360e5c3b";

        int row_ratings, row_movie = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String USER = "root";
            String PASSWORD = "12251225";

            // Connect to the test database
            Connection connection = DriverManager.getConnection
                    ("jdbc:mysql://10.81.1.123:4406/moviedb_api?useSSL=false",USER, PASSWORD);


            String movie_query = "SELECT * FROM movies WHERE id = ?";
            PreparedStatement count_stmt = connection.prepareStatement(movie_query);

            count_stmt.setString(1,movie_id);
            ResultSet result = count_stmt.executeQuery();

            String movie_title = "";
            Integer movie_year = 0;
            String movie_director ="";
            String movie_poster ="";
            String movie_plot ="";
            String movie_rated ="";
            String movie_runtime ="";
            String movie_language ="";
            String movie_writer ="";
            String movie_awards ="";
            String movie_boxOffice ="";
            String movie_production ="";
            String movie_country ="";

            //Movie Exists
            if(result.next()) {
                //Is movie cached in database? 1:Yes, 0:No
                movie_title = result.getString("title");
                movie_year = result.getInt("year");
                movie_director = result.getString("director");
                movie_poster = result.getString("poster");
                movie_plot = result.getString("plot");
                movie_rated = result.getString("rated");
                movie_runtime = result.getString("runtime");
                movie_language = result.getString("language");
                movie_writer = result.getString("writer");
                movie_awards = result.getString("awards");
                movie_boxOffice = result.getString("boxOffice");
                movie_production = result.getString("production");
                movie_country = result.getString("country");
                int cached = result.getInt("cached");
                if(cached >= 1){
                    return 0; //0 updates to database (Movie Exists and in Database)
                }
            }

            else {
                return 0;
            }

            // Create a neat value object to hold the URL
            URL url = new URL("http://www.omdbapi.com/?i="+movie_id+"&apikey="+API_KEY);

            // Open a connection(?) on the URL(??) and cast the response(???)
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Now it's "open", we can set the request method, headers etc.
            conn.setRequestProperty("accept", "application/json");

            // This line makes the request
            InputStream inputStream = conn.getInputStream();

            //Convert to String JSON
            String content = IOUtils.toString(inputStream);

            System.out.println(content);

            JsonObject json = new JsonParser().parse(content).getAsJsonObject();
            String title = json.get("Title").getAsString();
            String year = json.get("Year").getAsString();
            String rated = json.get("Rated").getAsString();
            String runtime = json.get("Runtime").getAsString();
            String director = json.get("Director").getAsString();
            String plot = json.get("Plot").getAsString();
            String language = json.get("Language").getAsString();
            String poster = json.get("Poster").getAsString();
            String box_office = json.get("BoxOffice").getAsString();
            String production = json.get("Production").getAsString();
            String country = json.get("Country").getAsString();
            String awards = json.get("Awards").getAsString();
            String writer = json.get("Writer").getAsString();


            JsonArray ratings = json.get("Ratings").getAsJsonArray();
            HashMap<String,String> movie_ratings = new HashMap<>();
            for(int i = 0; i < ratings.size(); i++) {
                JsonElement source = ratings.get(i).getAsJsonObject().get("Source");
                JsonElement value = ratings.get(i).getAsJsonObject().get("Value");

                movie_ratings.put(source.getAsString(),value.getAsString());
            }

            String imdb = movie_ratings.get("Internet Movie Database");
            String rotten_tom = movie_ratings.get("Rotten Tomatoes");
            String metacritic = movie_ratings.get("Metacritic");


            String query = "UPDATE movies SET plot=?, rated=?, runtime=?, language=?, poster=?, writer=?, awards=?, boxOffice=?, production=?, country=?, cached=?, title=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);

            if(movie_plot == null) {
                statement.setString(1,plot);
            }
            else {
                statement.setString(1,movie_plot);
            }

            if(movie_rated == null) {
                statement.setString(2,rated);
            }
            else {
                statement.setString(2,movie_rated);
            }

            if(movie_runtime == null) {
                statement.setString(3,runtime);
            }
            else {
                statement.setString(3,movie_runtime);
            }

            if(movie_language == null) {
                statement.setString(4,language);
            }
            else {
                statement.setString(4,movie_language);
            }

            if(movie_poster == null) {
                statement.setString(5,poster);
            }
            else {
                statement.setString(5,movie_poster);
            }

            if(movie_writer == null) {
                statement.setString(6,writer);
            }
            else {
                statement.setString(6,movie_writer);
            }

            if(movie_awards == null) {
                statement.setString(7,awards);
            }
            else {
                statement.setString(7,movie_awards);
            }

            if(movie_boxOffice == null) {
                statement.setString(8,box_office);
            }
            else {
                statement.setString(8,movie_boxOffice);
            }

            if(movie_production == null) {
                statement.setString(9,production);
            }
            else {
                statement.setString(9,movie_production);
            }

            if(movie_country == null) {
                statement.setString(10,country);
            }
            else {
                statement.setString(10,movie_country);
            }

            //Movie is cached to database
            statement.setInt(11,1);

            statement.setString(12, title);


            statement.setString(13, movie_id);
            row_movie = statement.executeUpdate();



            String ratings_query = "SELECT * FROM ratings WHERE movieId = ?";
            PreparedStatement ratings_stmt = connection.prepareStatement(ratings_query);

            ratings_stmt.setString(1,movie_id);
            result = ratings_stmt.executeQuery();

            //Movie Exists
            if(result.next()) {
                //Is movie cached in database? 1:Yes, 0:No
                metacritic = result.getString("metacritic");

            }

            query = "UPDATE ratings SET imdb=?, metacritic=?, rottenTomatoes=? WHERE movieId=?";
            statement = connection.prepareStatement(query);

            statement.setString(1,imdb);
            statement.setString(2,metacritic);
            statement.setString(3,rotten_tom.replace("%",""));
            statement.setString(4,movie_id);

            row_ratings = statement.executeUpdate();
            //connection.commit();
        }
        catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            //System.out.println(exceptionAsString);
            System.out.println("MOVIE ID " + movie_id);

        }

        return row_movie;
    }
     **/

    public void password()  {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String USER = "root";
            String PASSWORD = "12251225";

            // Connect to the test database
            Connection connection = DriverManager.getConnection
                    ("jdbc:mysql://10.81.1.123:4406/moviedb_api?useSSL=false",USER, PASSWORD);


            String query = "SELECT * FROM customers";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            ArrayList<Customer> customers = new ArrayList<>();

            while (result.next()) {
                Integer id = result.getInt("id");
                String password = result.getString("password");

                Customer cust = new Customer();
                cust.id = id;
                cust.password = password;
                customers.add(cust);
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            for(Customer c: customers) {
                Integer id = c.id;
                String encoded_password = passwordEncoder.encode(c.password);

                query = "UPDATE customers SET password=? WHERE id=?";
                statement = connection.prepareStatement(query);

                statement.setString(1,encoded_password);
                statement.setInt(2, id);

                Integer row = statement.executeUpdate();
            }

           // connection.commit();
        }
        catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            System.out.println(exceptionAsString);
        }
    }
}