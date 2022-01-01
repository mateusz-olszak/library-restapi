package com.library.aop;

import com.library.dao.CopiesAudRepository;
import com.library.dao.CopyRepository;
import com.library.domain.*;
import com.library.exceptions.ElementNotFoundException;
import com.library.status.Auditorium;
import com.library.status.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CopyAudWatcher {

    private final CopiesAudRepository copiesAudRepository;
    private final CopyRepository copyRepository;

    @After("execution(* com.library.service.CopyService.saveCopy(..))" +
            "&& args(copy)")
    public void saveCopyLogDb(Copy copy) {
        log.info("INSERT operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        CopiesAud copiesAud = buildInsertCopyAud(copy,readerDetails.getUsername());
        copiesAudRepository.save(copiesAud);
        log.info("INSERT operation is being recorded");
    }

    @Before("execution(* com.library.service.CopyService.changeCopyStatus(..))" +
            "&& args(id, status)")
    public void updateBookLogDb(int id, Status status) {
        log.info("UPDATE operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        if (copyRepository.existsById(id)) {
            Copy copy = copyRepository.findById(id).orElseThrow(ElementNotFoundException::new);
            CopiesAud copiesAud = buildUpdateCopyAud(copy, status, readerDetails.getUsername());
            copiesAudRepository.save(copiesAud);
            log.info("UPDATE operation is being recorded");
        }
    }

    @Before("execution(* com.library.service.CopyService.deleteCopy(..))" +
            "&& args(copyId)")
    public void deleteBookLogDb(int copyId) {
        log.info("DELETE operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        if (copyRepository.existsById(copyId)) {
            Copy copy = copyRepository.findById(copyId).orElseThrow(ElementNotFoundException::new);
            CopiesAud copiesAud = buildDeleteCopyAud(copy, readerDetails.getUsername());
            copiesAudRepository.save(copiesAud);
            log.info("DELETE operation is being recorded");
        }
    }

    private CopiesAud buildInsertCopyAud(Copy copy , String owner) {
        return CopiesAud.builder()
                .bookId(copy.getBook().getId())
                .copyId(copy.getId())
                .newStatus(copy.getStatus())
                .audType(Auditorium.INSERT)
                .audOwner(owner)
                .created(new Date())
                .build();
    }

    private CopiesAud buildUpdateCopyAud(Copy copy, Status newStatus, String owner) {
        return CopiesAud.builder()
                .bookId(copy.getBook().getId())
                .copyId(copy.getId())
                .oldStatus(copy.getStatus())
                .newStatus(newStatus)
                .audType(Auditorium.UPDATE)
                .audOwner(owner)
                .created(new Date())
                .build();
    }

    private CopiesAud buildDeleteCopyAud(Copy copy, String owner) {
        return CopiesAud.builder()
                .bookId(copy.getBook().getId())
                .copyId(copy.getId())
                .oldStatus(copy.getStatus())
                .audType(Auditorium.DELETE)
                .audOwner(owner)
                .created(new Date())
                .build();
    }
}
