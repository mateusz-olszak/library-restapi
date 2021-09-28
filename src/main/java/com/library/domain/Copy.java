package com.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamedQueries({
        @NamedQuery(
                name = "Copy.retrieveAvailableCopies",
                query = "FROM Copy WHERE status = :status"
        ),
        @NamedQuery(
                name = "Copy.retrieveCopiesWithGivenTitle",
                query = "FROM Copy WHERE book.title = :title"
        )
})
@Entity
@Table(name = "COPIES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Copy {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "COPY_ID")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @NotNull
    @Column(name = "STATUS")
    private String status;

    public Copy(Book book, String status) {
        this.book = book;
        this.status = status;
    }
}
