package com.epam.training.ticketservice.core.room.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    private String name;
    private int numRow;
    private int numCol;
    private String priceComponent = "";

    public Room(String name, int numRow, int numCol) {
        this.name = name;
        this.numRow = numRow;
        this.numCol = numCol;
    }
}
