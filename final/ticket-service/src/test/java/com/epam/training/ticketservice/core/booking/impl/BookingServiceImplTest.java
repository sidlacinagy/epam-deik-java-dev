package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.pricing.impl.PricingServiceImpl;
import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BookingServiceImplTest {
    private final PricingServiceImpl pricingService = mock(PricingServiceImpl.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final RoomServiceImpl roomService = mock(RoomServiceImpl.class);
    private final ScreeningServiceImpl screeningService = mock(ScreeningServiceImpl.class);
    private final MovieServiceImpl movieService = mock(MovieServiceImpl.class);

    private final BookingServiceImpl underTest=
            new BookingServiceImpl(bookingRepository,
                    roomService,screeningService,movieService,pricingService);

    @Test
    public void getPriceForBookingShouldReturnCorrectValue(){
        //Given
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.of(new Room("dummyRoom",5,6,"100")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.of(new Movie("dummyMovie","genre",12,"100")));
        when(screeningService.findById(new Screening.Key("dummyMovie","dummyRoom","2021-03-14 16:00")))
                .thenReturn(Optional.of(new Screening("dummyMovie","dummyRoom","2021-03-14 16:00","100")));
        when(pricingService.getValuebyName("100")).thenReturn(100);

        //When
        int result = underTest.getPriceForBooking(new Screening("dummyMovie",
                "dummyRoom", "2021-03-14 16:00", "100"));

        //Then
        assertEquals(1800,result);

    }

    @Test
    public void createBookingShouldReturnFailWhenScreeningDoesntExist(){
        //Given-When-Then
        assertEquals("Screening does not exist",underTest.createBooking(new BookingDto("dummy","dum",
                "dum","das",5,"fd")));
    }

    @Test
    public void createBookingShouldReturnFailWhenSeatDoesntExist(){
        //Given
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.of(new Room("dummyRoom",3,5,"100")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.of(new Movie("dummyMovie","genre",12,"100")));
        when(screeningService.findById(new Screening.Key("dummyMovie","dummyRoom","2021-03-14 16:00")))
                .thenReturn(Optional.of(new Screening("dummyMovie","dummyRoom","2021-03-14 16:00","100")));
        when(pricingService.getValuebyName("100")).thenReturn(100);

        //When
        String result1=underTest.createBooking(new BookingDto("dummy","dummyMovie",
                "dummyRoom","2021-03-14 16:00",5,"5,5 5,6"));
        String result2=underTest.createBooking(new BookingDto("dummy","dummyMovie",
                "dummyRoom","2021-03-14 16:00",5,"3,6 5,6"));
        String result3=underTest.createBooking(new BookingDto("dummy","dummyMovie",
                "dummyRoom","2021-03-14 16:00",5,"-1,6 5,6"));
        String result4=underTest.createBooking(new BookingDto("dummy","dummyMovie",
                "dummyRoom","2021-03-14 16:00",5,"2,-6 5,6"));

        //Then
        assertEquals("Seat (5,5) does not exist in this room",result1);
        assertEquals("Seat (3,6) does not exist in this room",result2);
        assertEquals("Seat (-1,6) does not exist in this room",result3);
        assertEquals("Seat (2,-6) does not exist in this room",result4);

    }

    @Test
    public void createBookingShouldReturnFailWhenSeatIsAlreadyBooked(){
        //Given
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.of(new Room("dummyRoom",7,8,"100")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.of(new Movie("dummyMovie","genre",12,"100")));
        when(screeningService.findById(new Screening.Key("dummyMovie","dummyRoom","2021-03-14 16:00")))
                .thenReturn(Optional.of(new Screening("dummyMovie","dummyRoom","2021-03-14 16:00","100")));
        when(pricingService.getValuebyName("100")).thenReturn(100);
        when(bookingRepository.findById(new Booking.Key("dummyMovie","dummyRoom","2021-03-14 16:00",5,5)))
                .thenReturn(Optional.of(new Booking("dummyName",5,"dummyMovie","dummyRoom",
                        "2021-03-14 16:00",5,5)));

        //When
        String result=underTest.createBooking(new BookingDto("dummy","dummyMovie",
                "dummyRoom","2021-03-14 16:00",5,"5,5 5,6"));


        //Then
        assertEquals("Seat (5,5) is already taken",result);

    }


    @Test
    public void createBookingShouldReturnCorrectStringWhenEverythingIsSuccessful(){
        //Given
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.of(new Room("dummyRoom",7,8,"100")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.of(new Movie("dummyMovie","genre",12,"100")));
        when(screeningService.findById(new Screening.Key("dummyMovie","dummyRoom","2021-03-14 16:00")))
                .thenReturn(Optional.of(new Screening("dummyMovie","dummyRoom","2021-03-14 16:00","100")));
        when(pricingService.getValuebyName("100")).thenReturn(100);

        //When
        String result=underTest.createBooking(new BookingDto("dummy","dummyMovie",
                "dummyRoom","2021-03-14 16:00",5,"5,5 5,6"));

        //Then
        assertEquals("Seats booked: (5,5), (5,6); the price for this booking is 3600 HUF",result);
    }

    @Test
    public void listBookingsForUserShouldReturnNoBookingsWhenUserHasNoBookings(){
        //Given
        when(screeningService.findAll()).thenReturn(List.of(new Screening("dummyMovie1","dummyRoom1","2021-03-14 16:00"),
                new Screening("dummyMovie2","dummyRoom2","2021-03-15 16:00")));
        when(bookingRepository.findByUser("dummy")).thenReturn(List.of());

        //When
        String result=underTest.listBookingsForUser("dummy");

        //Then
        assertEquals("You have not booked any tickets yet",result);


    }

    @Test
    public void listBookingsForUserShouldReturnAllBookingsWhenUserHasBookings(){
        //Given
        when(screeningService.findAll()).thenReturn(List.of(new Screening("dummyMovie1","dummyRoom1","2021-03-14 16:00"),
                new Screening("dummyMovie2","dummyRoom2","2021-03-15 16:00")));

        when(bookingRepository.findByUser("dummy")).thenReturn(List.of(new Booking()));

        when(bookingRepository.findByUserAndScreening("dummy","dummyRoom1","2021-03-14 16:00"))
                .thenReturn(List.of(new Booking("dummy",150,"dummyMovie1",
                        "dummyRoom1","2021-03-14 16:00",1,1),
                        new Booking("dummy",1250,"dummyMovie1",
                        "dummyRoom1","2021-03-14 16:00",2,2)));

        when(bookingRepository.findByUserAndScreening("dummy","dummyRoom2","2021-03-15 16:00"))
                .thenReturn(List.of(new Booking("dummy",250,"dummyMovie2",
                        "dummyRoom2","2021-03-15 16:00",3,3),
                        new Booking("dummy",200,"dummyMovie1",
                        "dummyRoom2","2021-03-15 16:00",4,4)));


        //When
        String result=underTest.listBookingsForUser("dummy");

        //Then
        assertEquals("Your previous bookings are\nSeats (1,1), (2,2) on dummyMovie1 in room " +
                "dummyRoom1 starting at 2021-03-14 16:00 for 1400 HUF\nSeats (3,3), (4,4) on dummyMovie2 in room " +
                "dummyRoom2 starting at 2021-03-15 16:00 for 450 HUF",result);

    }




}
