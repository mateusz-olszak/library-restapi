package com.library.integration;

import com.library.dao.*;
import com.library.domain.*;
import com.library.status.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReaderIntegrationTestSuite {
    
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CopyRepository copyRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private BookRepository bookRepository;
    
    @Test
    void testCreateReader() {
        // Given
        String password = "password";
        String encodedPassword = passwordEncoder.encode("password");
        Reader reader = new Reader("email@gmail.com",encodedPassword,new Date());
        // When
        Reader savedReader = readerRepository.save(reader);
        // Then
        assertThat(savedReader.getId()).isGreaterThan(0);
        assertThat(savedReader).isNotNull();
        assertThat(passwordEncoder.matches(password, savedReader.getPassword())).isEqualTo(true);
        // Cleanup
        readerRepository.deleteById(savedReader.getId());
    }

    @Test
    void testGetReader() {
        // Given
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        Reader reader = new Reader("email@gmail.com",encodedPassword,new Date());
        Reader savedReader = readerRepository.save(reader);
        // When
        Reader readerDb = readerRepository.findById(savedReader.getId()).get();
        // Then
        assertThat(readerDb).isNotNull();
        assertThat(readerDb.getId()).isGreaterThan(0);
        // Cleanup
        readerRepository.deleteById(readerDb.getId());
    }

    @Test
    void testFindReaderByEmail() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Reader reader = new Reader(email,encodedPassword,new Date());
        readerRepository.save(reader);
        // When
        Reader readerDb = readerRepository.findByEmail(email);
        // Then
        assertThat(readerDb).isNotNull();
        assertEquals(email,readerDb.getEmail());
        // Cleanup
        readerRepository.deleteById(readerDb.getId());
    }

    @Test
    void testGetReaders() {
        // Given
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        Reader reader1 = new Reader("1email@gmail.com",encodedPassword,new Date());
        Reader reader2 = new Reader("2email@gmail.com",encodedPassword,new Date());
        Reader reader3 = new Reader("3email@gmail.com",encodedPassword,new Date());
        Reader savedReader1 = readerRepository.save(reader1);
        Reader savedReader2 = readerRepository.save(reader2);
        Reader savedReader3 = readerRepository.save(reader3);
        // When
        List<Reader> readers = (List<Reader>) readerRepository.findAll();
        // Then
        assertEquals(3,readers.size());
        // Cleanup
        readerRepository.deleteById(savedReader1.getId());
        readerRepository.deleteById(savedReader2.getId());
        readerRepository.deleteById(savedReader3.getId());
    }

    @Test
    void testAssignRoleToReader() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Role role = new Role("Admin","Manage Everything");
        Role savedRole = roleRepository.save(role);
        Reader reader = new Reader(email,encodedPassword,new Date());
        // When
        reader.addRole(role);
        Reader savedReader = readerRepository.save(reader);
        // Then
        assertThat(savedReader.getRoles().contains(role)).isEqualTo(true);
        // Cleanup
        readerRepository.deleteById(savedReader.getId());
        roleRepository.deleteById(savedRole.getId());
    }

    @Test
    void testDeleteRole_readerShouldNotBeDeleted() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Role role = new Role("Admin","Manage Everything");
        Role savedRole = roleRepository.save(role);
        Reader reader = new Reader(email,encodedPassword,new Date());
        Reader savedReader = readerRepository.save(reader);
        // When
        roleRepository.deleteById(savedRole.getId());
        // Then
        boolean roleExists = roleRepository.existsById(savedRole.getId());
        boolean readerExists = readerRepository.existsById(savedReader.getId());
        assertThat(roleExists).isEqualTo(false);
        assertThat(readerExists).isEqualTo(true);
        // Cleanup
        readerRepository.deleteById(savedReader.getId());
    }

    @Test
    void testDeleteReader_roleShouldNotBeDeleted() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Role role = new Role("Admin","Manage Everything");
        Role savedRole = roleRepository.save(role);
        Reader reader = new Reader(email,encodedPassword,new Date());
        Reader savedReader = readerRepository.save(reader);
        // When
        readerRepository.deleteById(savedReader.getId());
        // Then
        boolean roleExists = roleRepository.existsById(savedRole.getId());
        boolean readerExists = readerRepository.existsById(savedReader.getId());
        assertThat(readerExists).isEqualTo(false);
        assertThat(roleExists).isEqualTo(true);
        // Cleanup
        roleRepository.deleteById(savedRole.getId());
    }

    @Test
    void testDeleteReader_rentalShouldBeDeleted() {
        // Given
        String password = "password";
        String email = "email@gmail.com";
        String encodedPassword = passwordEncoder.encode(password);
        Reader reader = new Reader(email,encodedPassword,new Date());
        Reader savedReader = readerRepository.save(reader);
        Book book = Book.builder().author("BookAuthor").title("BookTitle").description("BookDesc").yearOfPublication(1948).build();
        Book savedBook = bookRepository.save(book);
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        Rental rental = new Rental(copy,reader, LocalDate.now(),LocalDate.now().plusDays(7),Status.IN_USE);
        Rental savedRental = rentalRepository.save(rental);
        // When
        readerRepository.deleteById(savedReader.getId());
        // Then
        boolean rentalExists = rentalRepository.existsById(savedRental.getId());
        boolean readerExists = readerRepository.existsById(savedReader.getId());
        boolean copyExists = copyRepository.existsById(savedCopy.getId());
        boolean bookExists = bookRepository.existsById(savedBook.getId());
        assertThat(readerExists).isEqualTo(false);
        assertThat(rentalExists).isEqualTo(false);
        assertThat(copyExists).isEqualTo(true);
        assertThat(bookExists).isEqualTo(true);
        // Cleanup
        bookRepository.deleteById(savedBook.getId());
    }

    @Test
    void testCreateAdmin() {
        String password = "123";
        String encodedPass = passwordEncoder.encode(password);
        System.out.println(encodedPass);
        System.out.println(new Date());
        assertThat(passwordEncoder.matches(password,"$2a$10$az9qwhPq6cAcS9nwdP1cnub43ovfpeER8j7/v8GISurT5sYIND0oi")).isEqualTo(true);
    }

    @Test
    void testEncodePassword() {
        String password = "admin";
        String encodedPass = passwordEncoder.encode(password);
        System.out.println(encodedPass);
    }
}
