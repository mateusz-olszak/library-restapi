package com.library.aop;

import com.library.dao.BookRepository;
import com.library.dao.BooksAudRepository;
import com.library.domain.Book;
import com.library.domain.BooksAud;
import com.library.domain.ReaderDetails;
import com.library.dto.books.BookDto;
import com.library.exceptions.ElementNotFoundException;
import com.library.status.Auditorium;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class BookAudWatcher {

    private final BooksAudRepository booksAudRepository;
    private final BookRepository bookRepository;

    @After("execution(* com.library.service.BookService.saveBook(..))" +
            "&& args(book)")
    public void saveBookLogDb(Book book) {
        log.info("INSERT operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        BooksAud booksAud = buildSaveBookAud(book, readerDetails.getUsername());
        booksAudRepository.save(booksAud);
        log.info("INSERT operation is being recorded");
    }

    @Before("execution(* com.library.service.BookService.deleteBook(..))" +
            "&& args(bookId)")
    public void deleteBookLogDb(int bookId) {
        log.info("DELETE operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        Book book = bookRepository.findById(bookId).orElseThrow(ElementNotFoundException::new);
        BooksAud booksAud = buildDeleteBooksAud(book, readerDetails.getUsername());
        booksAudRepository.save(booksAud);
        log.info("DELETE operation is being recorded");
    }

    @Before("execution(* com.library.service.BookService.updateBook(..))" +
            "&& args(book)")
    public void updateBookLogDb(BookDto book) {
        log.info("UPDATE operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        if (bookRepository.existsById(book.getId())) {
            Book oldBook = bookRepository.findById(book.getId()).orElseThrow(ElementNotFoundException::new);
            BooksAud booksAud = buildUpdateBookAud(oldBook, book, readerDetails.getUsername());
            booksAudRepository.save(booksAud);
            log.info("UPDATE operation is being recorded");
        }
    }

    private BooksAud buildSaveBookAud(Book book, String owner) {
        return BooksAud.builder()
                .bookId(book.getId())
                .audOwner(owner)
                .created(new Date())
                .eventType(Auditorium.INSERT)
                .newPhoto(book.getPhoto())
                .newAuthor(book.getAuthor())
                .newTitle(book.getTitle())
                .newDescription(book.getDescription())
                .newYearOfPublication(book.getYearOfPublication())
                .newCurrency(book.getCurrency())
                .newPrice(book.getPrice())
                .build();
    }

    private BooksAud buildUpdateBookAud(Book oldBook, BookDto newBook, String owner) {
        return BooksAud.builder()
                .bookId(oldBook.getId())
                .audOwner(owner)
                .created(new Date())
                .eventType(Auditorium.UPDATE)
                .oldAuthor(oldBook.getAuthor())
                .newAuthor(newBook.getAuthor())
                .oldTitle(oldBook.getTitle())
                .newTitle(newBook.getTitle())
                .oldYearOfPublication(oldBook.getYearOfPublication())
                .newYearOfPublication(newBook.getYearOfPublication())
                .oldDescription(oldBook.getDescription())
                .newDescription(newBook.getDescription())
                .oldPrice(oldBook.getPrice())
                .newPrice(newBook.getPrice())
                .oldPhoto(oldBook.getPhoto())
                .newPhoto(newBook.getPhoto())
                .oldCurrency(oldBook.getCurrency())
                .newCurrency(newBook.getCurrency())
                .build();
    }

    private BooksAud buildDeleteBooksAud(Book book, String owner) {
        return BooksAud.builder()
                .bookId(book.getId())
                .audOwner(owner)
                .created(new Date())
                .eventType(Auditorium.DELETE)
                .oldAuthor(book.getAuthor())
                .oldTitle(book.getTitle())
                .oldDescription(book.getDescription())
                .oldYearOfPublication(book.getYearOfPublication())
                .oldPhoto(book.getPhoto())
                .oldPrice(book.getPrice())
                .oldCurrency(book.getCurrency())
                .build();
    }
}
