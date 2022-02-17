package com.moviedb_api.bookmark;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("BookmarkRepository")
public interface BookmarkRepository extends CrudRepository<Bookmark, Integer> {

    Page<Bookmark> findAll(Pageable pageable);

    Optional<Bookmark> findById(Integer id);

    Optional<Bookmark> findBookmarkByCustomerIdAndMovieId(Integer id, String movieId);

    Page<Bookmark> findBookmarkByCustomerId(Integer customerId, Pageable page);

    Page<Bookmark> findBookmarkByMovieId(String movieId, Pageable page);

    Iterable<Bookmark> findBookmarkByCustomerId(Integer customerId);

}