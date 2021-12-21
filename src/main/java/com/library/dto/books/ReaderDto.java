package com.library.dto.books;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ReaderDto {

    private int id;
    private String email;
    private String password;
    private Date created;

    public ReaderDto(int id, String email, String password, Date created) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.created = created;
    }

    public ReaderDto(String email, String password, Date created) {
        this.email = email;
        this.password = password;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Date getCreated() {
        return created;
    }
}
