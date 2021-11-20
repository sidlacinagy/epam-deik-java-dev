package com.epam.training.ticketservice.core.booking.model;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingDto {

    private String user;
    private String movieName;
    private String roomName;
    private String date;
    private int price;
    private String seats;


}
