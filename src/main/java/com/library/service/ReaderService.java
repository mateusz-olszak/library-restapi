package com.library.service;

import com.library.dao.ReaderRepository;
import com.library.dao.RoleRepository;
import com.library.domain.Reader;
import com.library.domain.Role;
import com.library.exceptions.ElementNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReaderService {

    private ReaderRepository readerRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public Reader saveReader(final Reader reader){
        String roleName = "Reader";
        Role role = roleRepository.findByRoleName(roleName);
        reader.addRole(role);
        encodePassword(reader);
        return readerRepository.save(reader);
    }

    public Reader findReaderById(final int id) throws ElementNotFoundException {
        return readerRepository.findById(id).orElseThrow(ElementNotFoundException::new);
    }

    public void deleteReader(final int id){
        readerRepository.deleteById(id);
    }

    public boolean isEmailUnique(String email) {
        Reader reader = readerRepository.findByEmail(email);
        return reader == null;
    }

    public Reader findReaderByEmail(String email) {
        return readerRepository.findByEmail(email);
    }

    private void encodePassword(Reader reader) {
        String encodedPassword = passwordEncoder.encode(reader.getPassword());
        reader.setPassword(encodedPassword);
    }
}
