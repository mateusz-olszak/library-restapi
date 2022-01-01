package com.library.domain;

import com.library.status.Auditorium;
import com.library.status.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "COPIES_AUD")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CopiesAud {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "COPY_ID")
    private Integer copyId;
    @Column(name = "BOOK_ID")
    private Integer bookId;
    @Column(name = "OLD_STATUS")
    private Status oldStatus;
    @Column(name = "NEW_STATUS")
    private Status newStatus;
    @Column(name = "CREATED")
    private Date created;
    @Column(name = "CHANGED_MADE_BY")
    private String audOwner;
    @Column(name = "EVENT_TYPE")
    private Auditorium audType;
}
