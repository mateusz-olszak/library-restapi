package com.library.domain;

import com.library.status.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "COPIES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Copy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "COPY_ID")
    private int id;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @NotNull
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Copy(Book book, Status status) {
        this.book = book;
        this.status = status;
    }
}
