package com.library.aop;

import com.library.dao.ReaderRepository;
import com.library.dao.ReadersAudRepository;
import com.library.domain.*;
import com.library.exceptions.ElementNotFoundException;
import com.library.status.Auditorium;
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
public class ReaderAudWatcher {

    private final ReaderRepository readerRepository;
    private final ReadersAudRepository readersAudRepository;

    @After("execution(* com.library.service.ReaderService.saveReader(..))" +
            "&& args(reader)")
    public void saveReaderLogDb(Reader reader) {
        log.info("INSERT operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        ReadersAud readersAud = buildInsertReaderAud(reader.getEmail(), readerDetails.getUsername());
        readersAudRepository.save(readersAud);
        log.info("INSERT operation is being recorded");
    }

    @Before("execution(* com.library.service.ReaderService.deleteReader(..))" +
            "&& args(readerId)")
    public void deleteBookLogDb(int readerId) {
        log.info("DELETE operation is being caught");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ReaderDetails readerDetails = (ReaderDetails) authentication.getPrincipal();
        Reader reader = readerRepository.findById(readerId).orElseThrow(ElementNotFoundException::new);
        ReadersAud readersAud = buildDeleteReadersAud(reader.getEmail(), readerDetails.getUsername());
        readersAudRepository.save(readersAud);
        log.info("DELETE operation is being recorded");
    }

    private ReadersAud buildInsertReaderAud(String newEmail, String owner) {
        return ReadersAud.builder()
                .newEmail(newEmail)
                .created(new Date())
                .eventType(Auditorium.INSERT)
                .audOwner(owner)
                .build();
    }

    private ReadersAud buildDeleteReadersAud(String oldEmail, String owner) {
        return ReadersAud.builder()
                .oldEmail(oldEmail)
                .created(new Date())
                .eventType(Auditorium.DELETE)
                .audOwner(owner)
                .build();
    }
}
