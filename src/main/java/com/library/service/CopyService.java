package com.library.service;

import com.library.dao.CopyRepository;
import com.library.domain.Book;
import com.library.domain.Copy;
import com.library.exceptions.ElementNotFoundException;
import com.library.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CopyService {

    private CopyRepository copyRepository;

    @Autowired
    public CopyService(CopyRepository copyRepository) {
        this.copyRepository = copyRepository;
    }

    public Copy saveCopy(final Copy copy){
        if (!copy.getStatus().equals(Status.AVAILABLE.name()) &&
            !copy.getStatus().equals(Status.DESTROYED.name()) &&
            !copy.getStatus().equals(Status.RENTED.name()) &&
            !copy.getStatus().equals(Status.LOST.name())
        )
            throw new IllegalArgumentException("Wrong copy status inserted");

        return copyRepository.save(copy);
    }

    public void saveAllCopies(List<Copy> copies){
        copyRepository.saveAll(copies);
    }

    public void deleteCopy(final int id){
        copyRepository.deleteById(id);
    }

    public Copy findCopy(final int id) throws ElementNotFoundException {
        return copyRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Could not find copy"));
    }

    public List<Copy> findAllCopies(){
        return (List<Copy>) copyRepository.findAll();
    }

    public List<Copy> retrieveAvailableCopies(String status){
        return copyRepository.retrieveAvailableCopies(status);
    }

    public List<Copy> retrieveCopiesWithGivenTitle(String title){
        return copyRepository.retrieveCopiesWithGivenTitle(title);
    }
}
