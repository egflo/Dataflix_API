package com.moviedb_api.bookmark;

import com.moviedb_api.HttpResponse;
import com.moviedb_api.cart.Cart;
import com.moviedb_api.cart.CartRepository;
import com.moviedb_api.cart.CartRequest;
import com.moviedb_api.inventory.Inventory;
import com.moviedb_api.inventory.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public ResponseEntity<?> bookmarkExists(BookmarkRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Bookmark not found");
        response.put("bookmark", null);

        Optional<Bookmark> bookmark = bookmarkRepository.findBookmarkByCustomerIdAndMovieId(request.getCustomerId(), request.getMovieId());
        if (bookmark.isPresent()) {
            response.put("success", true);
            response.put("message", "Bookmark found");
            response.put("bookmark", bookmark.get());

        }

        return ResponseEntity.ok(response);
        //HttpResponse response = new HttpResponse();
        //response.setMessage("Bookmark not found");
        //response.setStatus(404);
        //response.setSuccess(false);

        //return ResponseEntity.status(404).body(response);
    }

    public ResponseEntity<?> processBookmark(BookmarkRequest request) {

        System.out.println("userId: " + request.getCustomerId());
        System.out.println("movieId: " + request.getMovieId());

        Optional<Bookmark> bookmark = bookmarkRepository.findBookmarkByCustomerIdAndMovieId(request.getCustomerId(), request.getMovieId());
        Map<String, Object> response = new HashMap<>();

        if (bookmark.isPresent()) {
            bookmarkRepository.deleteById(bookmark.get().getId());
            response.put("success", true);
            response.put("message", "Bookmark Deleted");
            response.put("bookmark", bookmark.get());

            return ResponseEntity.ok(response);
        }

        Bookmark newBookmark = new Bookmark();
        newBookmark.setCustomerId(request.getCustomerId());
        newBookmark.setMovieId(request.getMovieId());
        newBookmark.setCreated(new Date());

        Bookmark savedBookmark = bookmarkRepository.save(newBookmark);
        System.out.println("savedBookmark: " + savedBookmark);

        response.put("success", true);
        response.put("message", "Bookmark Added");
        response.put("bookmark", savedBookmark);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getBookmark(BookmarkRequest request) {
        Optional<Bookmark> bookmark = bookmarkRepository.findById(request.getId());
        if (bookmark.isPresent()) {
            return ResponseEntity.ok(bookmark.get());
        }
        HttpResponse response = new HttpResponse();
        response.setMessage("Bookmark not found");
        response.setStatus(404);
        response.setSuccess(false);

        return ResponseEntity.status(404).body(response);
    }

    public ResponseEntity<?> getBookmarksByCustomerId(BookmarkRequest request, Pageable pageable) {

        Page<Bookmark> bookmarks = bookmarkRepository.findBookmarkByCustomerId(request.getCustomerId(), pageable);
        return ResponseEntity.ok(bookmarks);
    }

    public ResponseEntity<?> getBookmarksByMovieId(BookmarkRequest request, Pageable pageable) {

        Page<Bookmark> bookmarks = bookmarkRepository.findBookmarkByMovieId(request.getMovieId(), pageable);
        return ResponseEntity.ok(bookmarks);
    }


    public ResponseEntity<?> getAllBookmarks(PageRequest pageable) {

        Page<Bookmark> bookmarks = bookmarkRepository.findAll(pageable);
        return ResponseEntity.ok(bookmarks);
    }

    public ResponseEntity<?> addBookmark(BookmarkRequest request) {

        Optional<Bookmark> bookmark = bookmarkRepository.findBookmarkByCustomerIdAndMovieId(request.getId(), request.getMovieId());

        if(bookmark.isPresent()) {
            HttpResponse response = new HttpResponse();
            response.setMessage("Bookmark already exists");
            response.setStatus(409);
            response.setSuccess(false);
            return ResponseEntity.status(409).body(response);
        }

        else {
            Bookmark newBookmark = new Bookmark();
            newBookmark.setCustomerId(request.getCustomerId());
            newBookmark.setMovieId(request.getMovieId());
            newBookmark.setCreated(new Date());

            return new ResponseEntity<>(
                    bookmarkRepository.save(newBookmark),
                    HttpStatus.CREATED); //Resource Created
        }
    }

    public ResponseEntity<?> updateBookmarkByCustomerIdandMovieId(BookmarkRequest request) {
        Optional<Bookmark> bookmark = bookmarkRepository.findBookmarkByCustomerIdAndMovieId(request.getCustomerId(), request.getMovieId());

        if (bookmark.isPresent()) {

            return deleteBookmark(request);
        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Bookmark not found");
        response.setStatus(404);
        response.setSuccess(false);
        return ResponseEntity.status(404).body(response);

    }

    public ResponseEntity<?> updateBookmark(BookmarkRequest request) {
        Optional<Bookmark> bookmark = bookmarkRepository.findById(request.getId());
        if (bookmark.isPresent()) {
            Bookmark updatedBookmark = bookmark.get();
            updatedBookmark.setCustomerId(request.getCustomerId());
            updatedBookmark.setMovieId(request.getMovieId());
            updatedBookmark.setCreated(new Date());

            return ResponseEntity.ok(bookmarkRepository.save(updatedBookmark));
        }

        HttpResponse response = new HttpResponse();
        response.setMessage("Bookmark not found");
        response.setStatus(404);
        response.setSuccess(false);

        return ResponseEntity.status(404).body(response);

    }

    public ResponseEntity<?> deleteBookmark(BookmarkRequest request) {
        //Optional<Bookmark> bookmark = bookmarkRepository.findById(request.getId());
        Optional<Bookmark> bookmark = bookmarkRepository.findBookmarkByCustomerIdAndMovieId(request.getCustomerId(), request.getMovieId());

        if (bookmark.isPresent()) {
            bookmarkRepository.delete(bookmark.get());

            HttpResponse response = new HttpResponse();
            response.setMessage("Bookmark deleted");
            response.setStatus(200);
            response.setSuccess(true);
            return ResponseEntity.status(200).body(response);
        }
        HttpResponse response = new HttpResponse();
        response.setMessage("Bookmark not found");
        response.setStatus(404);
        response.setSuccess(false);
        return ResponseEntity.status(404).body(response);
    }
}
