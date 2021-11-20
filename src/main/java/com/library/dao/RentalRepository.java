package com.library.dao;

import com.library.domain.Reader;
import com.library.domain.Rental;
import com.library.status.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface RentalRepository extends CrudRepository<Rental, Integer> {
    List<Rental> findAllByReader(Reader reader);
}
