package com.epam.training.ticketservice.core.screening.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;



@Entity
@Data
@NoArgsConstructor
public class Screening {

    @EmbeddedId
    private Key key;

    public Screening(String movieName, String roomName, String date) {
        this.key = new Key(movieName,roomName,date);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Key implements Serializable {
        @Column(nullable = false)
        private String movieName;

        @Column(nullable = false)
        private String roomName;

        @Column(nullable = false)
        private String date;
    }


}
