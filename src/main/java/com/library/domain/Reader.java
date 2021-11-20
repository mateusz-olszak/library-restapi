package com.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "READERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "READER_ID")
    private int id;

    @Column(name = "EMAIL", length = 50, unique = true)
    @NotNull
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "CREATED")
    private Date created;

    @OneToMany(
            targetEntity = Rental.class,
            mappedBy = "reader",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Rental> rental;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USERS_ROLES",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Reader(String email, String password, Date created) {
        this.email = email;
        this.password = password;
        this.created = created;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}
