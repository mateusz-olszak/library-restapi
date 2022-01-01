package com.library.service;

import com.library.dao.ReaderRepository;
import com.library.domain.Reader;
import com.library.domain.ReaderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

public class ReaderDetailsService implements UserDetailsService {

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Reader reader = readerRepository.findByEmail(email);
        if (reader != null) {
            return new ReaderDetails(reader);
        }
        throw new UsernameNotFoundException("Could not find reader with given email: " + email);
    }
}
