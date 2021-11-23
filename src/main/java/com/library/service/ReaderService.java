package com.library.service;

import com.library.dao.ReaderRepository;
import com.library.dao.RoleRepository;
import com.library.domain.Reader;
import com.library.domain.Role;
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
        String readerRole = "Reader";
        String adminRole = "Admin";
        if (reader.getEmail().equals("admin@gmail.com")){
            Role role = roleRepository.findByRoleName(adminRole);
            reader.addRole(role);
            encodePassword(reader);
            return readerRepository.save(reader);
        } else {
            Role role = roleRepository.findByRoleName(readerRole);
            reader.addRole(role);
            encodePassword(reader);
            return readerRepository.save(reader);
        }
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
