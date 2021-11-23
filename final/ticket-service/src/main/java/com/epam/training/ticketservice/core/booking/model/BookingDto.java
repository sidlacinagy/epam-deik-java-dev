package com.epam.training.ticketservice.core.booking.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {

    private String user;
    private String movieName;
    private String roomName;
    private String date;
    private int price;
    private String seats;


}
