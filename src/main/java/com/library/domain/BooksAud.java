package com.library.domain;

import com.library.status.Auditorium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BOOKS_AUD")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BooksAud {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "BOOK_ID")
    private int bookId;
    @Column(name = "EVENT_TYPE")
    @Enumerated(EnumType.STRING)
    private Auditorium eventType;
    @Column(name = "CHANGES_MADE_BY")
    private String audOwner;
    @Column(name = "CREATED")
    private Date created;
    @Column(name = "OLD_PHOTO")
    private String oldPhoto;
    @Column(name = "NEW_PHOTO")
    private String newPhoto;
    @Column(name = "OLD_TITLE")
    private String oldTitle;
    @Column(name = "NEW_TITLE")
    private String newTitle;
    @Column(name = "OLD_AUTHOR")
    private String oldAuthor;
    @Column(name = "NEW_AUTHOR")
    private String newAuthor;
    @Column(name = "OLD_YEAR_OF_PUBLICATION")
    private int oldYearOfPublication;
    @Column(name = "NEW_YEAR_OF_PUBLICATION")
    private int newYearOfPublication;
    @Column(name = "OLD_DESCRIPTION")
    private String oldDescription;
    @Column(name = "NEW_DESCRIPTION")
    private String newDescription;
    @Column(name = "OLD_PRICE")
    private double oldPrice;
    @Column(name = "NEW_PRICE")
    private double newPrice;
    @Column(name = "OLD_CURRENCY")
    private String oldCurrency;
    @Column(name = "NEW_CURRENCY")
    private String newCurrency;
}
