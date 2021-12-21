package com.library.dto.books;


import com.library.status.Status;

public class CopyDto {

    private int id;
    private int bookId;
    private Status status;

    public CopyDto(int id, int bookId, Status status) {
        this.id = id;
        this.bookId = bookId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public Status getStatus() {
        return status;
    }
}
