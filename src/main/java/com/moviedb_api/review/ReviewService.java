package com.moviedb_api.review;

import com.moviedb_api.HttpResponse;
import org.json.HTTP;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    @PersistenceContext
    private EntityManager entityManager;

    private ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public ResponseEntity<?> create(ReviewRequest reviewRequest) {
        System.out.println(reviewRequest.getMovieId());
        System.out.println(reviewRequest.getCustomerId());

        Review review = new Review();
        review.setMovieId(reviewRequest.getMovieId());
        review.setCustomerId(reviewRequest.getCustomerId());
        review.setText(reviewRequest.getText());
        review.setTitle(reviewRequest.getTitle());
        review.setRating(reviewRequest.getRating());
        review.setCreated(new Date(System.currentTimeMillis()));

        String sentiment = "positive";
        if (reviewRequest.getRating() < 5) {
            sentiment = "negative";
        }
        review.setSentiment(sentiment);

        Review save = reviewRepository.save(review);
        entityManager.flush();
        save = reviewRepository.findById(save.getId()).get();
        return ResponseEntity.ok(save);
    }

    public ResponseEntity<?> update(ReviewRequest reviewRequest) {

        Optional<Review> reviewOptional = reviewRepository.findById(reviewRequest.getId());
        if (reviewOptional.isPresent()) {
            Review reviewToUpdate = reviewOptional.get();
            reviewToUpdate.setTitle(reviewRequest.getTitle());
            reviewToUpdate.setText(reviewRequest.getText());
            reviewToUpdate.setRating(reviewRequest.getRating());

            String sentiment = "positive";
            if (reviewRequest.getRating() < 5) {
                sentiment = "negative";
            }
            reviewToUpdate.setSentiment(sentiment);
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Review not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> delete(ReviewRequest reviewRequest) {

        Optional<Review> reviewOptional = reviewRepository.findById(reviewRequest.getId());
        if (reviewOptional.isPresent()) {
            Review reviewToDelete = reviewOptional.get();
            reviewRepository.delete(reviewToDelete);

            return ResponseEntity.ok().build();
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Review not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<?> findById(Integer id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            return ResponseEntity.ok(reviewOptional.get());
        }

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setMessage("Review not found");
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpResponse.setSuccess(false);

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> findByMovieId(String movieId, PageRequest pageRequest) {
        return ResponseEntity.ok(reviewRepository.findReviewByMovieId(movieId, pageRequest));
    }

    public ResponseEntity<?> findByCustomerId(Integer customerId, PageRequest pageRequest) {
        return ResponseEntity.ok(reviewRepository.findReviewByCustomerId(customerId, pageRequest));
    }

    public ResponseEntity<?> findAll(PageRequest pageRequest) {
        return ResponseEntity.ok(reviewRepository.findAll(pageRequest));
    }

    private Boolean isNumber(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public ResponseEntity<?> search(String search, PageRequest pageRequest) {
        if(isNumber(search)) {
            return new ResponseEntity<>(
                    reviewRepository.findReviewByCustomerId(Integer.parseInt(search), pageRequest),
                    HttpStatus.OK); //Resource Found

        }
        else {
            return new ResponseEntity<>(
                    reviewRepository.findReviewByMovieId(search, pageRequest),
                    HttpStatus.OK); //Resource Found

        }    }

}

