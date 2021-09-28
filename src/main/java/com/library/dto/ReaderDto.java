package com.library.dto;

import com.library.domain.Reader;

import java.util.Date;

public class ReaderDto {

    private int id;
    private String firstName;
    private String lastName;
    private Date created;

    public ReaderDto(int id, String firstName, String lastName, Date created) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getCreated() {
        return created;
    }
}
