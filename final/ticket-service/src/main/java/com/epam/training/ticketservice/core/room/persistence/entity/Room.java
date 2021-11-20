package com.epam.training.ticketservice.core.room.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Room {

    @Id
    private String name;
    private int rows;
    private int columns;


}
