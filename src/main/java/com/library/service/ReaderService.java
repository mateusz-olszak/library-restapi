package com.library.service;

import com.library.dao.ReaderRepository;
import com.library.domain.Reader;
import com.library.exceptions.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {

    private ReaderRepository readerRepository;

    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public Reader saveReader(final Reader reader){
        return readerRepository.save(reader);
    }

    public Reader findReaderById(final int id) throws ElementNotFoundException {
        return readerRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Could not find reader."));
    }

    public List<Reader> findAllReaders(){
        return (List<Reader>) readerRepository.findAll();
    }

    public void deleteReader(final int id){
        readerRepository.deleteById(id);
    }

}
