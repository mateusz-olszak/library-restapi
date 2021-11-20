package com.library.domain;

import lombok.AllArgsConstructor;
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

    @OneToMany(
            targetEntity = Copy.class,
            mappedBy = "book",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Copy> copy = new ArrayList<>();

    public Book(String title, String author, int yearOfPublication) {
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
    }

    public Book(String photo, String title, String author, int yearOfPublication, String description) {
        this.photo = photo;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.description = description;
    }

    public String getBookThumbnail() {
        if (photo == null)
            return "/images/default-book.png";
        return "/book-thumbnails/" + this.getId() + "/" + this.photo;
    }

    public void addCopy(Copy copy) {
        this.copy.add(copy);
    }
}
