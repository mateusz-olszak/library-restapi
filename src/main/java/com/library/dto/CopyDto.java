package com.library.dto;


public class CopyDto {

    private int id;
    private String bookTitle;
    private String bookAuthor;
    private int bookYearOfPublication;
    private String status;

    public CopyDto(int id, String bookTitle, String bookAuthor, int bookYearOfPublication, String status) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookYearOfPublication = bookYearOfPublication;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public int getBookYearOfPublication() {
        return bookYearOfPublication;
    }

    public String getStatus() {
        return status;
    }
}
