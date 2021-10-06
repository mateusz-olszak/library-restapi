package com.library.dao;

import com.library.domain.Copy;
import com.library.status.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface CopyRepository extends CrudRepository<Copy, Integer> {

    @Query
    List<Copy> retrieveCopiesWithGivenTitle(String title);

    @Query
    List<Copy> retrieveCopiesForGivenBook(int id);

    @Query
    List<Copy> retrieveAvailableCopiesForGivenId(int id);
}
