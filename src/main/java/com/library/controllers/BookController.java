package com.library.controllers;

import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.dto.BookDto;
import com.library.dto.RentalDto;
import com.library.exceptions.ElementNotFoundException;
import com.library.mappers.BookMapper;
import com.library.service.BookService;
import com.library.service.CopyService;
import com.library.service.FileUploadService;
import com.library.status.Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

import static com.library.service.BookService.BOOKS_PER_PAGE;

@Controller
@AllArgsConstructor
@Slf4j
public class BookController {

    private BookService bookService;
    private BookMapper bookMapper;
    private FileUploadService fileUploadService;
    private CopyService copyService;

    @GetMapping("/books")
    String viewHomePage(Model model){
        return listByPageBooks(1, null, model);
    }

    @GetMapping("/books/{page}")
    String listByPageBooks(
            @PathVariable("page") int page,
            @Param("keyword") String keyword,
            Model model
    ) {
        Page<Book> pageBooks = bookService.findAllBooks(page, keyword);
        List<BookDto> books = bookMapper.mapToListBookDto(pageBooks.getContent());
        int totalPages = pageBooks.getTotalPages();
        long startCount = (page-1) * BOOKS_PER_PAGE + 1;
        long endCount = startCount + BOOKS_PER_PAGE - 1;
        if (endCount > pageBooks.getTotalElements()) {
            endCount = pageBooks.getTotalElements();
        }
        long totalElements = pageBooks.getTotalElements();
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("keyword", keyword);
        model.addAttribute("books",books);
        return "index";
    }

    @GetMapping("/books/view/{id}")
    String viewBook(@PathVariable("id") int id, Model model) throws ElementNotFoundException {
        BookDto book = bookMapper.mapToBookDto(bookService.findBook(id));
        int copies = copyService.retrieveAvailableCopiesForGivenId(id).size();
        boolean isDescEmpty = Objects.isNull(book.getDescription());
        RentalDto rentalDto = new RentalDto();
        model.addAttribute("book", book);
        model.addAttribute("copies",copies);
        model.addAttribute("isDescEmpty", isDescEmpty);
        model.addAttribute("rental",rentalDto);
        return "view_book";
    }

    // NEW BOOK
    @GetMapping("/books/new")
    String getNewBookTemplate(Model model){
        BookDto bookDto = new BookDto();
        model.addAttribute("book", bookDto);
        return "new_book";
    }

    @PostMapping("/books/new")
    String saveBook(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("yearOfPublication") int yearOfPublication,
            @RequestParam("copies") int copies,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile multipartFile
    ) {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            BookDto bookDto = new BookDto(fileName,title,author,yearOfPublication,description);
            Book book = bookService.saveBook(bookMapper.mapToBook(bookDto));
            if (copies > 0) {
                for (int i=0; i<copies; i++) {
                    copyService.saveCopy(new Copy(book, Status.AVAILABLE));
                }
            }
            String uploadDir = "book-thumbnails/" + book.getId();
            fileUploadService.saveFile(uploadDir,fileName,multipartFile);

            return "redirect:/books";
        } else {
            BookDto bookDto = new BookDto(title, author, yearOfPublication, description);
            Book book = bookService.saveBook(bookMapper.mapToBook(bookDto));
            if (copies > 0) {
                for (int i=0; i<copies; i++) {
                    copyService.saveCopy(new Copy(book, Status.AVAILABLE));
                }
            }
            return "redirect:/books";
        }
    }

    @GetMapping("/books/edit/{id}")
    String editBook(@PathVariable("id") int id, Model model) throws ElementNotFoundException {
        Book book = bookService.findBook(id);
        BookDto bookDto = bookMapper.mapToBookDto(book);
        model.addAttribute("book",bookDto);
        return "edit_book";
    }

    @PostMapping("/books/edit/save")
    String updateBook(
            @ModelAttribute BookDto bookDto,
            RedirectAttributes redirectAttributes,
            @RequestParam("image") MultipartFile multipartFile
    ) throws ElementNotFoundException
    {
        log.info("Preparing to update the book.");
        if  (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            bookDto.setPhoto(fileName);
            Book book = bookService.updateBook(bookDto);
            copyService.updateAmountOfCopies(book, bookDto.getCopies());
            String uploadDir = "book-thumbnails/" + book.getId();
            fileUploadService.saveFile(uploadDir,fileName,multipartFile);
            log.info("Saving book with new image");
        } else {
            log.info("book with the same image is about to be saved, book id: " + bookDto.getId());
            Book book = bookService.updateBook(bookDto);
            copyService.updateAmountOfCopies(book, bookDto.getCopies());
        }
        redirectAttributes.addFlashAttribute("message", "The book has been updated successfully.");
        return "redirect:/books";
    }

    @RequestMapping("/books/delete/{id}")
    String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
