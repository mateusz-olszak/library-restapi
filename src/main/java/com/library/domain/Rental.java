package com.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "RENTALS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "RENTAL_ID")
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "COPY_ID")
    private Copy copy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "READER_ID")
    private Reader reader;

    @NotNull
    @Column(name = "RENATAL_FROM")
    private LocalDate rentedFrom;

    @NotNull
    @Column(name = "RENATAL_TO")
    private LocalDate rentedTo;

    public Rental(Copy copy, Reader reader, LocalDate rentedFrom, LocalDate rentedTo) {
        this.copy = copy;
        this.reader = reader;
        this.rentedFrom = rentedFrom;
        this.rentedTo = rentedTo;
    }
}
