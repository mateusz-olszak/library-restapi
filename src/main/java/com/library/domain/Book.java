package com.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NamedQuery(
        name = "Book.retrieveAllBooksMatchingKeyword",
        query = "FROM Book b WHERE b.title LIKE concat('%',concat(:keyword,'%')) OR b.author LIKE concat('%',concat(:keyword,'%'))"
)
@Entity
@Table(name = "BOOKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "BOOK_ID")
    private int id;

    @Column(name = "BOOK_THUMBNAIL")
    private String photo;

    @NotNull
    @Column(name = "TITLE", length = 50)
    private String title;

    @NotNull
    @Column(name = "AUTHOR", length = 50)
    private String author;

    @NotNull
    @Column(name = "PUBLICATION_YEAR")
    private int yearOfPublication;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "CURRENCY")
    private String currency;

    @OneToMany(
            targetEntity = Copy.class,
            mappedBy = "book",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private final List<Copy> copy = new ArrayList<>();
}
