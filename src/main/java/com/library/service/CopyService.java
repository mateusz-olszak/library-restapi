package com.library.service;

import com.library.dao.CopyRepository;
import com.library.domain.Copy;
import com.library.exceptions.ElementNotFoundException;
import com.library.status.Status;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        )
            throw new IllegalArgumentException("Wrong copy status inserted");

        return copyRepository.save(copy);
    }

    public void saveAllCopies(List<Copy> copies){
        for (Copy copy : copies){
            if (!copy.getStatus().equals(Status.AVAILABLE) &&
                    !copy.getStatus().equals(Status.DESTROYED) &&
                    !copy.getStatus().equals(Status.RENTED) &&
                    !copy.getStatus().equals(Status.LOST)
            )
                throw new IllegalArgumentException("Wrong copy status inserted");
        }
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

    public List<Copy> retrieveAvailableCopies(){
        return copyRepository.retrieveAvailableCopies();
    }

    public List<Copy> retrieveCopiesWithGivenTitle(String title){
        return copyRepository.retrieveCopiesWithGivenTitle(title);
    }

    public Integer retrieveCopiesForGivenBook(final int id){
        Integer copiesId = Math.toIntExact(copyRepository.retrieveCopiesForGivenBook(id).stream().map(copy -> copy.getId()).mapToInt(Integer::intValue).count());
        return copiesId;
    }

    public List<Copy> retrieveAvailableCopiesForGivenTitle(String title){
        return copyRepository.retrieveAvailableCopiesForGivenTitle(title);
    }
}
