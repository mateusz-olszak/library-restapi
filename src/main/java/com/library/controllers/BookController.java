package com.library.controllers;

import com.library.dto.books.BookDto;
import com.library.service.ModelFillerService;
import com.library.service.facade.BookFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookFacade bookFacade;
    private final ModelFillerService modelFillerService;

    @GetMapping("/")
    String redirectToHomePage(Model model) {
        return viewHomePage(model);
    }

    @GetMapping("/books")
    String viewHomePage(Model model) {
        return listByPageBooks(1, null, model);
    }

    @GetMapping("/books/{page}")
    String listByPageBooks(@PathVariable("page") int page, @Param("keyword") String keyword, Model model) {
        Map<String, Object> modelMap = bookFacade.listBooksByPageWithKeyword(keyword, page);
        modelFillerService.fullFillModel(model,modelMap);
        return "index";
    }

    @GetMapping("/books/view/{id}")
    String viewBook(@PathVariable("id") int id, Model model) {
        Map<String, Object> modelMap = bookFacade.viewBookById(id);
        modelFillerService.fullFillModel(model,modelMap);
        return "view_book";
    }

    @GetMapping("/books/new")
    String getNewBookTemplate(Model model) {
        Map<String, Object> modelMap = bookFacade.printNewBookTemplate();
        modelFillerService.fullFillModel(model,modelMap);
        return "new_book";
    }

    @PostMapping("/books/new")
    String saveBook(@ModelAttribute BookDto tempBook) {
        bookFacade.saveBook(tempBook);
        return "redirect:/books";
    }

    @GetMapping("/books/edit/{id}")
    String editBook(@PathVariable("id") int id, Model model) {
        Map<String, Object> modelMap = bookFacade.printEditPage(id);
        modelFillerService.fullFillModel(model,modelMap);
        return "edit_book";
    }

    @PostMapping("/books/edit/save")
    String updateBook(@ModelAttribute BookDto bookDto) {
        bookFacade.updateBook(bookDto);
        return "redirect:/books";
    }

    @RequestMapping("/books/delete/{id}")
    String deleteBook(@PathVariable("id") int id) {
        bookFacade.deleteBook(id);
        return "redirect:/books";
    }


}
