package com.library.domain;

import com.library.status.Auditorium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "RENTALS_AUD")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentsAud {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "DATE")
    private Date date;
    @Column(name = "EVENT_TYPE")
    @Enumerated(EnumType.STRING)
    private Auditorium eventType;
    @Column(name = "RENT_ID")
    private int rentalId;
    @Column(name = "OLD_COPY_ID")
    private int oldCopyId;
    @Column(name = "NEW_COPY_ID")
    private int newCopyId;
    @Column(name = "OLD_READER_ID")
    private int oldReaderId;
    @Column(name = "NEW_READER_ID")
    private int newReaderId;
    @Column(name = "OLD_RENT_FROM")
    private LocalDate oldRentFrom;
    @Column(name = "NEW_RENT_FROM")
    private LocalDate newRentFrom;
    @Column(name = "OLD_RETURN")
    private LocalDate oldReturn;
    @Column(name = "NEW_RETURN")
    private LocalDate newReturn;
    @Column(name = "CHANGES_MADE_BY")
    private String audOwner;
}
