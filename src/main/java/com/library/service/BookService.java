package com.library.service;

import com.library.dao.BookRepository;
import com.library.domain.Book;
import com.library.exceptions.ElementNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    private BookRepository bookRepository;

    public Book saveBook(final Book book){
        return bookRepository.save(book);
    }

    public void deleteBook(final int id){
        bookRepository.deleteById(id);
    }

    public Book findBook(final int id) throws ElementNotFoundException {
        return bookRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Could not find book."));
    }

    public List<Book> findAllBooks(){
        return (List<Book>) bookRepository.findAll();
    }
}
