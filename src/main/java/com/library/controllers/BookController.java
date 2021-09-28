package com.library.controllers;

import com.library.domain.Book;
import com.library.dto.BookDto;
import com.library.exceptions.ElementNotFoundException;
import com.library.mappers.BookMapper;
import com.library.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    private BookService bookService;
    private BookMapper bookMapper;

    @GetMapping("/books")
    List<BookDto> getAllBooks(){
        return bookMapper.mapToListReaderDto(bookService.findAllBooks());
    }

    @GetMapping("/book")
    BookDto getBook(@RequestParam("id") int id) throws ElementNotFoundException {
        Book book = bookService.findBook(id);
        return bookMapper.mapToBookDto(book);
    }

    @PostMapping("/books")
    void saveBook(@RequestBody BookDto bookDto){
        Book book = bookMapper.mapToBook(bookDto);
        bookService.saveBook(book);
    }

    @DeleteMapping("/book/delete/{id}")
    void deleteBook(@PathVariable int id){
        bookService.deleteBook(id);
    }

}
