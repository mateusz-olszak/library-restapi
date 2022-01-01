package com.library.dao;

import com.library.domain.CopiesAud;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CopiesAudRepository extends CrudRepository<CopiesAud, Integer> {
}
