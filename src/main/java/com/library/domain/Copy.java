package com.library.domain;

import com.library.status.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamedQueries({
        @NamedQuery(
                name = "Copy.retrieveAvailableCopiesForGivenId",
                query = "FROM Copy WHERE status = 'AVAILABLE' AND book.id = :id"
        ),
        @NamedQuery(
                name = "Copy.retrieveCopiesWithGivenTitle",
                query = "FROM Copy WHERE book.title = :title"
        ),
        @NamedQuery(
                name = "Copy.retrieveCopiesForGivenBook",
                query = "FROM Copy WHERE book.id = :id"
        )
})
@Entity
@Table(name = "COPIES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Copy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "COPY_ID")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @NotNull
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Copy(Book book, Status status) {
        this.book = book;
        this.status = status;
    }
}
