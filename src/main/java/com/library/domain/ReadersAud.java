package com.library.domain;

import com.library.status.Auditorium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "READERS_AUD")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadersAud {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "OLD_EMAIL")
    private String oldEmail;
    @Column(name = "NEW_EMAIL")
    private String newEmail;
    @Column(name = "CREATED")
    private Date created;
    @Column(name = "CHANGES_MADE_BY")
    private String audOwner;
    @Column(name = "EVENT_TYPE")
    private Auditorium eventType;
}
