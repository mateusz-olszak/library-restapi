package com.library.dao;

import com.library.domain.Reader;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ReaderRepository extends PagingAndSortingRepository<Reader,Integer> {
    Reader findByEmail(String email);
}
