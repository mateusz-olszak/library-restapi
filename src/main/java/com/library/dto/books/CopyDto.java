package com.library.dto.books;


import com.library.status.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CopyDto {
    private int id;
    private int bookId;
    private Status status;
}
