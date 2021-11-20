package com.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ROLES")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private int id;

    @NotNull
    @Column(name = "ROLE_NAME", length = 100, unique = true)
    private String roleName;

    @NotNull
    @Column(name = "ROLE_DESCRIPTION")
    private String descritpion;

    public Role(String roleName, String descritpion) {
        this.roleName = roleName;
        this.descritpion = descritpion;
    }
}
