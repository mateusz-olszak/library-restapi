package com.library.dao;

import com.library.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Integer> {
    @Query
    Page<Book> retrieveAllBooksMatchingKeyword(@Param("keyword") String keyword, Pageable pageable);
}
