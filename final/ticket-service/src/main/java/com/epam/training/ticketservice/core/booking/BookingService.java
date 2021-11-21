package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;

import java.util.List;

public interface BookingService {

    String createBooking(BookingDto bookingDto);

    String listBookingsForUser(String user);

    void changePrice(int price);

    int getPriceForBooking(Screening screening);
}
