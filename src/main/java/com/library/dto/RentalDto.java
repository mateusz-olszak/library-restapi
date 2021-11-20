package com.library.dto;

import com.library.status.Status;

import java.time.LocalDate;

public class RentalDto {

    private int id;
    private int copyId;
    private int readerId;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;
    private Status completed;

    public RentalDto() {
    }

    public RentalDto(int id, int copyId, int readerId, LocalDate rentedFrom, LocalDate rentedTo, Status completed) {
        this.id = id;
        this.copyId = copyId;
        this.readerId = readerId;
        this.rentedFrom = rentedFrom;
        this.rentedTo = rentedTo;
        this.completed = completed;
    }

    public RentalDto(int copyId, int readerId) {
        this.copyId = copyId;
        this.readerId = readerId;
    }

    public int getId() {
        return id;
    }

    public int getCopyId() {
        return copyId;
    }

    public int getReaderId() {
        return readerId;
    }

    public LocalDate getRentedFrom() {
        return rentedFrom;
    }

    public LocalDate getRentedTo() {
        return rentedTo;
    }

    public Status getCompleted() {
        return completed;
    }
}
