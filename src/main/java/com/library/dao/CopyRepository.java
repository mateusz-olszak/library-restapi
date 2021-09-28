package com.library.dao;

import com.library.domain.Copy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface CopyRepository extends CrudRepository<Copy, Integer> {

    @Query
    List<Copy> retrieveAvailableCopies(String status);

    @Query
    List<Copy> retrieveCopiesWithGivenTitle(String title);
}
