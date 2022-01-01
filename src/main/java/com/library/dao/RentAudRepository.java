package com.library.dao;

import com.library.domain.RentsAud;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RentAudRepository extends CrudRepository<RentsAud, Integer> {
}
