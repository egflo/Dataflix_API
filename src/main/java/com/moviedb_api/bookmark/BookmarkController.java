package com.moviedb_api.bookmark;

import com.moviedb_api.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


//https://www.amitph.com/spring-rest-http-header/
@RestController
@RequestMapping("/bookmark")
public class BookmarkController {


    @Autowired
    private JwtTokenUtil authenticationService;

    @Autowired
    private BookmarkService bookmarkService;


    /**
     *
     *    USER METHODS
     * **/

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserBookmarkById(@RequestHeader HttpHeaders headers,
                                             @PathVariable String id) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);

        BookmarkRequest request = new BookmarkRequest();
        request.setMovieId(id);
        request.setCustomerId(Integer.parseInt(userId));



        return bookmarkService.bookmarkExists(request);
        //return bookmarkService.getBookmark(request);
    }

    @PutMapping("/{id})")
    public ResponseEntity<?> addUserBookmark(@RequestHeader HttpHeaders headers,
                                             @PathVariable String id) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);

        BookmarkRequest request = new BookmarkRequest();
        request.setMovieId(id);
        request.setCustomerId(Integer.parseInt(userId));

        return bookmarkService.addBookmark(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserBookmark(@RequestHeader HttpHeaders headers,
                                                @PathVariable String id) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);

        BookmarkRequest request = new BookmarkRequest();
        request.setMovieId(id);
        request.setCustomerId(Integer.parseInt(userId));

        return bookmarkService.deleteBookmark(request);
    }

    @GetMapping("/")
    public ResponseEntity<?> getUserBookmark(@RequestHeader HttpHeaders headers,
                                             @RequestBody BookmarkRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setCustomerId(Integer.parseInt(userId));

        return bookmarkService.getBookmark(request);
    }

    @PutMapping("/")
    public ResponseEntity<?> addUserBookmark(@RequestHeader HttpHeaders headers,
                                      @RequestBody BookmarkRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setCustomerId(Integer.parseInt(userId));

        return bookmarkService.addBookmark(request);
    }

    @PostMapping("/")
    public ResponseEntity<?> updateUserBookmark(@RequestHeader HttpHeaders headers,
                                             @RequestBody BookmarkRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setCustomerId(Integer.parseInt(userId));

        return bookmarkService.processBookmark(request);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteBookmark(@RequestHeader HttpHeaders headers,
                                            @RequestBody BookmarkRequest request) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        request.setCustomerId(Integer.parseInt(userId));

        return bookmarkService.deleteBookmark(request);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUserBookmarks(
            @RequestHeader HttpHeaders headers,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy
    ) {

        String token = headers.get("authorization").get(0).split(" ")[1].trim();
        String userId = authenticationService.getUserId(token);
        BookmarkRequest request = new BookmarkRequest();
        request.setCustomerId(Integer.parseInt(userId));

        return bookmarkService.getBookmarksByCustomerId(request, PageRequest.of(
                page.orElse(0),
                limit.orElse(5),
                Sort.Direction.DESC, sortBy.orElse("created")
        ));
    }


    /**
     *
     *    ADMIN METHODS
     * **/


    /*
    @GetMapping("/{id}")
    public ResponseEntity<?> findByBookmarkId(
            @PathVariable Integer id) {

        BookmarkRequest request = new BookmarkRequest();
        request.setId(id);
        return bookmarkService.getBookmark(request);
    }
*/
    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getBookmarksByCustomerId(
            @PathVariable Integer id,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy) {

        BookmarkRequest request = new BookmarkRequest();
        request.setCustomerId(id);

        return bookmarkService.getBookmarksByCustomerId(request, PageRequest.of(
                page.orElse(0),
                limit.orElse(5),
                Sort.Direction.ASC, sortBy.orElse("id")
        ));
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<?> getBookmarksByMovieId(
            @PathVariable String id,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy) {

        BookmarkRequest request = new BookmarkRequest();
        request.setMovieId(id);

        return bookmarkService.getBookmarksByMovieId(request, PageRequest.of(
                page.orElse(0),
                limit.orElse(5),
                Sort.Direction.ASC, sortBy.orElse("id")
        ));
    }

    @PutMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addBookmark(@RequestBody BookmarkRequest request) {

        return bookmarkService.addBookmark(request);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteBookmark(@RequestBody BookmarkRequest request) {

        return bookmarkService.deleteBookmark(request);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateBookmark(@RequestBody BookmarkRequest request) {

        return bookmarkService.updateBookmark(request);
    }

}
