package com.library.controllers;

import com.library.dto.books.CopyDto;
import com.library.service.facade.CopyFacade;
import com.library.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CopyController {

    private final CopyFacade copyFacade;

    @GetMapping("/copies")
    List<CopyDto> getAllCopies(){
        return copyFacade.getAllCopies();
    }

    @GetMapping(value = "/copies", params = "id")
    List<CopyDto> getAllCopiesForGivenBook(@RequestParam("id") int id){
        return copyFacade.getAllCopiesForGivenBookId(id);
    }

    @GetMapping("copies/{id}")
    CopyDto getCopy(@PathVariable("id") int id) {
        return copyFacade.getCopy(id);
    }

    @PostMapping("/copies")
    void saveCopy(@RequestBody CopyDto copyDto) {
        copyFacade.saveCopy(copyDto);
    }

    @DeleteMapping("/copies/{id}")
    void deleteCopy(@PathVariable int id) {
        copyFacade.deleteCopy(id);
    }

    @PatchMapping("/copies/{id}")
    CopyDto changeCopyStatus(@PathVariable int id, @RequestParam("status")Status status) {
        return copyFacade.changeCopyStatus(id, status);
    }

}
