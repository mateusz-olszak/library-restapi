package com.library.service;

import com.library.dao.CopyRepository;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.exceptions.ElementNotFoundException;
import com.library.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableAspectJAutoProxy
public class CopyService {

    private final CopyRepository copyRepository;

    public Copy saveCopy(final Copy copy){
        if (!copy.getStatus().equals(Status.AVAILABLE) &&
            !copy.getStatus().equals(Status.DESTROYED) &&
            !copy.getStatus().equals(Status.RENTED) &&
            !copy.getStatus().equals(Status.LOST)
        ) {
            throw new IllegalArgumentException("Wrong copy status inserted");
        }
        return copyRepository.save(copy);
    }

    public void deleteCopy(int id){
        copyRepository.deleteById(id);
    }

    public Copy findCopy(int id) {
        return copyRepository.findById(id).orElseThrow(ElementNotFoundException::new);
    }

    public List<Copy> findAllCopies(){
        return (List<Copy>) copyRepository.findAll();
    }

    public Integer retrieveAmountOfCopiesForGivenBook(int id){
        return Math.toIntExact(copyRepository.findCopiesByBook_Id(id).stream().map(Copy::getId).mapToInt(Integer::intValue).count());
    }

    public void deleteCopiesByBookIdAndStatus(int id, Status status) {
        copyRepository.deleteAllByBook_IdAndStatus(id, status);
    }

    public List<Copy> retrieveAvailableCopiesForGivenId(int id){
        return copyRepository.findCopiesByStatusAndAndBook_Id(Status.AVAILABLE,id);
    }

    public Copy changeCopyStatus(int id, Status status) {
        Copy copy = copyRepository.findById(id).orElseThrow(ElementNotFoundException::new);
        copy.setStatus(status);
        return copyRepository.save(copy);
    }

    public void updateAmountOfCopies(Book book, int amountOfCopies) {
        if (amountOfCopies > 0) {
            int bookId = book.getId();
            deleteCopiesByBookIdAndStatus(bookId, Status.AVAILABLE);
            for (int i = 0; i < amountOfCopies; i++) {
                copyRepository.save(new Copy(book,Status.AVAILABLE));
            }
        }
    }

    public List<Copy> findAllAvailableBooks(Status status) {
        return copyRepository.findAllByStatus(status);
    }
}
