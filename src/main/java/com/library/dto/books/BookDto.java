package com.library.dto.books;

import com.library.dto.googleapi.GoogleVolumeInfoDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
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

    public BookDto(String photo, String title, String author, int yearOfPublication, String description, double price, String currency, GoogleVolumeInfoDto googleVolumeInfoDto) {
        this.title = title;
        this.photo = photo;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.googleVolumeInfoDto = googleVolumeInfoDto;
    }

    public BookDto(String title, String author, int yearOfPublication, String description, double price, String currency, GoogleVolumeInfoDto googleVolumeInfoDto) {
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.googleVolumeInfoDto = googleVolumeInfoDto;
    }

    public String getBookThumbnail() {
        if (photo == null)
            return "/images/default-book.png";
        return "/book-thumbnails/" + this.getId() + "/" + this.photo;
    }
}
