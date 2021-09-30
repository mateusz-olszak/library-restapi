package com.library.dto;

import com.library.domain.Copy;

import java.util.List;

public class BookDto {

    private int id;
    private String title;
    private String author;
    private int yearOfPublication;
    private Integer copies;

    public BookDto(int id, String title, String author, int yearOfPublication, Integer copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.copies = copies;
    }

    public BookDto(String title, String author, int yearOfPublication) {
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
    }

    public BookDto() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public Integer getCopies() {
        return copies;
    }
}
