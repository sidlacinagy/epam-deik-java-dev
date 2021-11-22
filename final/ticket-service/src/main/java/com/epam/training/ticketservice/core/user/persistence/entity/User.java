package com.epam.training.ticketservice.core.user.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;



    public enum Role {
        USER,
        ADMIN
    }
}
