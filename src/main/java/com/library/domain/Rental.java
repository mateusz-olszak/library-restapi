package com.library.domain;

import com.library.status.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "RENTALS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "RENTAL_ID")
    private int id;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "COPY_ID")
    private Copy copy;

    @ManyToOne
    @JoinColumn(name = "READER_ID")
    private Reader reader;

    @NotNull
    @Column(name = "RENATAL_FROM")
    private LocalDate rentedFrom;

    @NotNull
    @Column(name = "RENATAL_TO")
    private LocalDate rentedTo;

    @Enumerated(EnumType.STRING)
    private Status completed;

    public Rental(Copy copy, Reader reader, LocalDate rentedFrom, LocalDate rentedTo, Status completed) {
        this.copy = copy;
        this.reader = reader;
        this.rentedFrom = rentedFrom;
        this.rentedTo = rentedTo;
        this.completed = completed;
    }

    public Rental(Copy copy, Reader reader) {
        this.copy = copy;
        this.reader = reader;
    }
}
