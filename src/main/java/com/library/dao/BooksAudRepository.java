package com.library.dao;

import com.library.domain.BooksAud;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BooksAudRepository extends CrudRepository<BooksAud, Integer> {
}
