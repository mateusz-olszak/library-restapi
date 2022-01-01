package com.library.service;

import com.library.dao.BookRepository;
import com.library.domain.Book;
import com.library.dto.books.BookDto;
import com.library.exceptions.ElementNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
@EnableAspectJAutoProxy
public class BookService {

    public static final int BOOKS_PER_PAGE = 5;

    private final BookRepository bookRepository;

    public Book saveBook(final Book book){
        return bookRepository.save(book);
    }

    public void deleteBook(final int id){
        bookRepository.deleteById(id);
    }

    public Book findBook(final int id) {
        return bookRepository.findById(id).orElseThrow(ElementNotFoundException::new);
    }

    public Page<Book> findAllBooks(int pageNum, String keyword){
        Pageable pageable = PageRequest.of(pageNum - 1, BOOKS_PER_PAGE);
        if (keyword != null) {
            return bookRepository.retrieveAllBooksMatchingKeyword(keyword,pageable);
        }
        return bookRepository.findAll(pageable);
    }

    public Book updateBook(BookDto bookDto) {
        Book book = bookRepository.findById(bookDto.getId()).orElseThrow(ElementNotFoundException::new);
        book.setAuthor(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setYearOfPublication(bookDto.getYearOfPublication());
        book.setPrice(bookDto.getPrice());
        book.setCurrency(bookDto.getCurrency());
        book.setPhoto(bookDto.getPhoto().isEmpty() ? null : bookDto.getPhoto());
        return bookRepository.save(book);
    }
}
