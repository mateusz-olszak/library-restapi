package com.library.dao;

import com.library.domain.ReadersAud;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ReadersAudRepository extends CrudRepository<ReadersAud, Integer> {
}
