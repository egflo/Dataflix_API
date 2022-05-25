package com.moviedb_api.bookmark;

import com.moviedb_api.HttpResponse;
import com.moviedb_api.cart.Cart;
import com.moviedb_api.cart.CartRepository;
import com.moviedb_api.cart.CartRequest;
import com.moviedb_api.inventory.Inventory;
import com.moviedb_api.inventory.InventoryService;
import com.moviedb_api.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private AuthenticationFacade authenticationFacade;

    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public ResponseEntity<?> getBookmark(String movieId) {

        Optional<Bookmark> bookmark = bookmarkRepository.findBookmarkByCustomerIdAndMovieId(authenticationFacade.getUserId(), movieId);
        HttpResponse response = new HttpResponse();

        if (bookmark.isPresent()) {
            response.setStatus(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setMessage("Bookmark Exists");
            response.setData(bookmark.get());

            return ResponseEntity.ok(response);
        }

        //Bookmark does not exist
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setSuccess(false);
        response.setMessage("Bookmark does not exist");
        response.setData(null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<?> updateBookmark(BookmarkRequest request) {

        Optional<Bookmark> bookmark = bookmarkRepository.findBookmarkByCustomerIdAndMovieId(request.getCustomerId(), request.getMovieId());

        //If user role is not admin, check if the user has already bookmarked the movie
        if (authenticationFacade.hasRole("USER")) {
            bookmark = bookmarkRepository.findBookmarkByCustomerIdAndMovieId(authenticationFacade.getUserId(), request.getMovieId());
        }

        HttpResponse response = new HttpResponse();

        if (bookmark.isPresent()) {
            bookmarkRepository.deleteById(bookmark.get().getId());
            response.setStatus(HttpStatus.OK.value());
            response.setSuccess(true);
            response.setMessage("Bookmark Removed");
            response.setData(null);

            return ResponseEntity.ok(response);
        }

        //Bookmark does not exist so create it
        Bookmark newBookmark = new Bookmark();
        newBookmark.setCustomerId(authenticationFacade.hasRole("USER") ? authenticationFacade.getUserId() : request.getCustomerId());
        newBookmark.setMovieId(request.getMovieId());
        newBookmark.setCreated(new Date());
        Bookmark savedBookmark = bookmarkRepository.save(newBookmark);

        response.setStatus(HttpStatus.OK.value());
        response.setSuccess(true);
        response.setMessage("Bookmark Added");
        response.setData(savedBookmark);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> deleteBookmark(BookmarkRequest request) {
        //Optional<Bookmark> bookmark = bookmarkRepository.findById(request.getId());
        Optional<Bookmark> bookmark = bookmarkRepository.findBookmarkByCustomerIdAndMovieId(request.getCustomerId(), request.getMovieId());

        //If user role is not admin, check if the user has already bookmarked the movie to be deleted
        if(authenticationFacade.hasRole("USER")) {
            bookmark = bookmarkRepository.findBookmarkByCustomerIdAndMovieId(authenticationFacade.getUserId(), request.getMovieId());
        }

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

        request.setCustomerId( authenticationFacade.getUserId());

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

}
