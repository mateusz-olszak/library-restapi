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

    public ReaderDto(String email, String password, Date created) {
        this.email = email;
        this.password = password;
        this.created = created;
    }
}
