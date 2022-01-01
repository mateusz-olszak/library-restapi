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

    List<Copy> findCopiesByBook_Title(String title);

    List<Copy> findCopiesByBook_Id(int id);

    List<Copy> findCopiesByStatusAndAndBook_Id(Status status, int id);

    List<Copy> findAllByStatus(Status status);

    void deleteAllByBook_IdAndStatus(int id, Status status);
}
