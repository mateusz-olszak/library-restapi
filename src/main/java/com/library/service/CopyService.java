package com.library.service;

import com.library.dao.CopyRepository;
import com.library.domain.Copy;
import com.library.exceptions.ElementNotFoundException;
import com.library.status.Status;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CopyService {

    private CopyRepository copyRepository;

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

    public void deleteAllCopiesMatchingBookId(int id) {
        copyRepository.deleteAllByBook_Id(id);
    }

    public Copy findCopy(int id) throws ElementNotFoundException {
        return copyRepository.findById(id).orElseThrow(ElementNotFoundException::new);
    }

    public List<Copy> findAllCopies(){
        return (List<Copy>) copyRepository.findAll();
    }

    public Integer retrieveAmountOfCopiesForGivenBook(int id){
        return Math.toIntExact(copyRepository.findCopiesByBook_Id(id).stream().map(Copy::getId).mapToInt(Integer::intValue).count());
    }

    public List<Copy> retrieveAvailableCopiesForGivenId(int id){
        return copyRepository.findCopiesByStatusAndAndBook_Id(Status.AVAILABLE,id);
    }

    public Copy changeCopyStatus(int id, Status status) throws ElementNotFoundException {
        Copy copy = copyRepository.findById(id).orElseThrow(ElementNotFoundException::new);
        copy.setStatus(status);
        return copyRepository.save(copy);
    }
}
