package com.library.dto.books;

import com.library.dto.googleapi.GoogleVolumeInfoDto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookDto {

    private int id;
    private String title;
    private String author;
    private String description;
    private double price;
    private String currency;
    private int yearOfPublication;
    private GoogleVolumeInfoDto googleVolumeInfoDto;
    private Integer copies;
    private String photo;
}
