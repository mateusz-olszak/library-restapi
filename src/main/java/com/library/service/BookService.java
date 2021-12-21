package com.library.service;

import com.library.dao.BookRepository;
import com.library.domain.Book;
import com.library.dto.books.BookDto;
import com.library.exceptions.ElementNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    public static final int BOOKS_PER_PAGE = 5;

    private final BookRepository bookRepository;

    public Book saveBook(final Book book){
        return bookRepository.save(book);
    }

    public void deleteBook(final int id){
        bookRepository.deleteById(id);
    }

    public Book findBook(final int id) throws ElementNotFoundException {
        return bookRepository.findById(id).orElseThrow(ElementNotFoundException::new);
    }

    public Page<Book> findAllBooks(int pageNum, String keyword){
        Pageable pageable = PageRequest.of(pageNum - 1, BOOKS_PER_PAGE);
        if (keyword != null) {
            return bookRepository.retrieveAllBooksMatchingKeyword(keyword,pageable);
        }
        return bookRepository.findAll(pageable);
    }

    public Book updateBook(BookDto bookDto) throws ElementNotFoundException {
        log.info("Preparing to update book");
        Book book = bookRepository.findById(bookDto.getId()).orElseThrow(ElementNotFoundException::new);
        book.setAuthor(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setYearOfPublication(bookDto.getYearOfPublication());
        book.setPhoto(bookDto.getPhoto());
        return bookRepository.save(book);
    }
}
