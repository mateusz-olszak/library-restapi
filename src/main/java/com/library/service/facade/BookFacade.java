package com.library.service.facade;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.dto.books.BookDto;
import com.library.dto.books.RentalDto;
import com.library.mappers.BookMapper;
import com.library.service.BookService;
import com.library.service.CopyService;
import com.library.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.library.service.BookService.BOOKS_PER_PAGE;

@Component
@RequiredArgsConstructor
public class BookFacade {

    private final BookMapper bookMapper;
    private final BookService bookService;
    private final CopyService copyService;

    public void saveBook(BookDto bookDto) {
        Book book = bookService.saveBook(bookMapper.mapToBook(bookDto));
        if (bookDto.getCopies() > 0) {
            for (int i = 0; i < bookDto.getCopies(); i++) {
                copyService.saveCopy(new Copy(book, Status.AVAILABLE));
            }
        }
    }

    public void updateBook(BookDto bookDto) {
        Book book = bookService.updateBook(bookDto);
        copyService.updateAmountOfCopies(book, bookDto.getCopies());
    }

    public Map<String, Object> printEditPage(int id) {
        Map<String, Object> modelMap = new HashMap<>();
        Book book = bookService.findBook(id);
        BookDto bookDto = bookMapper.mapToBookDto(book);
        modelMap.put("book",bookDto);
        return modelMap;
    }

    public Map<String, Object> printNewBookTemplate() {
        Map<String, Object> modelMap = new HashMap<>();
        BookDto bookDto = new BookDto();
        modelMap.put("book",bookDto);
        return modelMap;
    }

    public Map<String, Object> viewBookById(int bookId) {
        Map<String, Object> modelMap = new HashMap<>();
        BookDto book = bookMapper.mapToBookDto(bookService.findBook(bookId));
        int copies = copyService.retrieveAvailableCopiesForGivenId(bookId).size();
        boolean isDescEmpty = Objects.isNull(book.getDescription());
        RentalDto rentalDto = new RentalDto();
        modelMap.put("book",book);
        modelMap.put("copies",copies);
        modelMap.put("isDescEmpty",isDescEmpty);
        modelMap.put("rental",rentalDto);
        return modelMap;
    }

    public Map<String,Object> listBooksByPageWithKeyword(String keyword, int page) {
        Page<Book> pageBooks = bookService.findAllBooks(page, keyword);
        List<BookDto> books = new ArrayList<>(bookMapper.mapToListBookDto(pageBooks.getContent()));
        int totalPages = pageBooks.getTotalPages();
        long startCount = (long) (page - 1) * BOOKS_PER_PAGE + 1;
        long endCount = startCount + BOOKS_PER_PAGE - 1;
        if (endCount > pageBooks.getTotalElements()) {
            endCount = pageBooks.getTotalElements();
        }
        long totalElements = pageBooks.getTotalElements();
        Map<String,Object> modelMap = new HashMap<>();
        modelMap.put("startCount", startCount);
        modelMap.put("endCount", endCount);
        modelMap.put("currentPage",page);
        modelMap.put("totalPages",totalPages);
        modelMap.put("totalElements",totalElements);
        modelMap.put("keyword",keyword);
        modelMap.put("books",books);
        return modelMap;
    }

    public void deleteBook(int bookId) {
        bookService.deleteBook(bookId);
    }
}
