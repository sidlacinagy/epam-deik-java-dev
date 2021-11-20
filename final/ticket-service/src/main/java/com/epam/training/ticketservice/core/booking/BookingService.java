package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

import java.util.List;

public interface BookingService {

    String createBooking(BookingDto bookingDto);

    String listBookingsForUser(String user);
}
