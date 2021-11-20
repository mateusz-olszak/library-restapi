package com.library.dto;

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
    private int yearOfPublication;
    private Integer copies;
    private String photo;

    public BookDto(String photo, String title, String author, int yearOfPublication, String description) {
        this.title = title;
        this.photo = photo;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.description = description;
    }

    public BookDto(String title, String author, int yearOfPublication, String description) {
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.description = description;
    }

    public String getBookThumbnail() {
        if (photo == null)
            return "/images/default-book.png";
        return "/book-thumbnails/" + this.getId() + "/" + this.photo;
    }
}
