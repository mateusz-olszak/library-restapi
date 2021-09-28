package com.library.dto;

import com.library.domain.Reader;

import java.time.LocalDate;

public class RentalDto {

    private int id;
    private BookDto book;
    private ReaderDto reader;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;

    public RentalDto(int id, BookDto book, ReaderDto reader, LocalDate rentedFrom, LocalDate rentedTo) {
        this.id = id;
        this.book = book;
        this.reader = reader;
        this.rentedFrom = rentedFrom;
        this.rentedTo = rentedTo;
    }

    public int getId() {
        return id;
    }

    public BookDto getBook() {
        return book;
    }

    public ReaderDto getReader() {
        return reader;
    }

    public LocalDate getRentedFrom() {
        return rentedFrom;
    }

    public LocalDate getRentedTo() {
        return rentedTo;
    }
}
