package com.library.integration.aop;

import com.library.dao.BookRepository;
import com.library.dao.CopiesAudRepository;
import com.library.dao.CopyRepository;
import com.library.domain.Book;
import com.library.domain.CopiesAud;
import com.library.domain.Copy;
import com.library.service.CopyService;
import com.library.status.Auditorium;
import com.library.status.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CopyAudIntegrationTestSuite {

    @Autowired
    private CopyRepository copyRepository;
    @Autowired
    private CopyService copyService;
    @Autowired
    private CopiesAudRepository copiesAudRepository;
    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@gmail.com");
    }

    @Test
    void testSaveCopyAud() {
        // Given
        Book book = Book.builder().author("Author")
                .title("Title").yearOfPublication(1948)
                .description("Description").price(20.0)
                .currency("PLN").photo("photo").build();
        Book savedBook = bookRepository.save(book);
        int bookId = savedBook.getId();
        Copy copy = new Copy(book, Status.AVAILABLE);
        // When
        copyService.saveCopy(copy);
        // Then
        List<CopiesAud> savedAuds = (List<CopiesAud>) copiesAudRepository.findAll();
        CopiesAud saveAud = savedAuds.stream().findFirst().get();
        int audId = saveAud.getId();

        assertEquals(1,savedAuds.size());
        assertEquals("test@gmail.com",saveAud.getAudOwner());
        assertEquals(Auditorium.INSERT, saveAud.getAudType());

        bookRepository.deleteById(bookId);
        copiesAudRepository.deleteById(audId);
    }

    @Test
    void testDeleteCopyAud() {
        // Given
        Book book = Book.builder().author("Author")
                .title("Title").yearOfPublication(1948)
                .description("Description").price(20.0)
                .currency("PLN").photo("photo").build();
        Book savedBook = bookRepository.save(book);
        int bookId = savedBook.getId();
        Copy copy = new Copy(book, Status.AVAILABLE);
        Copy savedCopy = copyService.saveCopy(copy);
        int copyId = savedCopy.getId();
        // When
        copyService.deleteCopy(copyId);
        // Then
        List<CopiesAud> savedAuds = (List<CopiesAud>) copiesAudRepository.findAll();
        CopiesAud saveCopyAud = savedAuds.get(0);
        CopiesAud deleteCopyAud = savedAuds.get(1);

        assertEquals(2,savedAuds.size());
        assertEquals("test@gmail.com", deleteCopyAud.getAudOwner());
        assertEquals(Auditorium.DELETE, deleteCopyAud.getAudType());

        bookRepository.deleteById(bookId);
        copiesAudRepository.deleteById(saveCopyAud.getId());
        copiesAudRepository.deleteById(deleteCopyAud.getId());
    }

    @Test
    void testUpdateCopyAud() {
        // Given
        Book book = Book.builder().author("Author")
                .title("Title").yearOfPublication(1948)
                .description("Description").price(20.0)
                .currency("PLN").photo("photo").build();
        Book savedBook = bookRepository.save(book);
        int savedBookId = savedBook.getId();
        Copy copy = new Copy(savedBook, Status.AVAILABLE);
        Copy savedCopy = copyRepository.save(copy);
        int copyId = savedCopy.getId();
        // When
        copyService.changeCopyStatus(copyId, Status.RENTED);
        // Then
        List<CopiesAud> savedAuds = (List<CopiesAud>) copiesAudRepository.findAll();
        CopiesAud updateCopyAud = savedAuds.get(0);
        assertEquals(1,savedAuds.size());
        assertEquals("test@gmail.com", updateCopyAud.getAudOwner());
        assertEquals(Auditorium.UPDATE, updateCopyAud.getAudType());

        bookRepository.deleteById(savedBookId);
        copiesAudRepository.deleteById(updateCopyAud.getId());
    }
}
