package com.epam.training.ticketservice.core.booking.persistence.entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @EmbeddedId
    private Key key;
    private String user;
    private int price;

    public Booking(String user, int price, String movieName, String roomName, String date, int rowNum, int colNum) {
        this.user = user;
        this.price = price;
        this.key = new Key(movieName, roomName, date, rowNum, colNum);
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

        @Column(nullable = false)
        private int rowNum;

        @Column(nullable = false)
        private int colNum;


    }
}
