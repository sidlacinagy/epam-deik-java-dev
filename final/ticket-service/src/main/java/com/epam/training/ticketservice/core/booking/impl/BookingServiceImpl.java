package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private RoomService roomService;
    private MovieService movieService;
    private ScreeningService screeningService;
    private UserService userService;


    public BookingServiceImpl(BookingRepository bookingRepository,
                              RoomService roomService,
                              MovieService movieService,
                              ScreeningService screeningService, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.userService = userService;
    }


    @Override
    public String createBooking(BookingDto bookingDto) {
        Objects.requireNonNull(bookingDto, "Booking cannot be null");
        Objects.requireNonNull(bookingDto.getSeats(), "Seats cannot be null");
        Objects.requireNonNull(bookingDto.getDate(), "Date cannot be null");
        Objects.requireNonNull(bookingDto.getMovieName(), "Movie name cannot be null");
        Objects.requireNonNull(bookingDto.getRoomName(), "Room name cannot be null");
        Objects.requireNonNull(bookingDto.getUser(), "User cannot be null");

        Optional<Room> roomByName = roomService.getRoomByName(bookingDto.getRoomName());
        if (roomByName.isEmpty()) {
            return "Room does not exist";
        }

        int roomRow = roomByName.get().getNumRow();
        int roomCol = roomByName.get().getNumCol();

        String[] seats = bookingDto.getSeats().split(" ");
        List<Booking> approvedBookings = new ArrayList<>();
        StringBuilder seatsAsReturnString = new StringBuilder();
        seatsAsReturnString.append("Seats booked: ");
        for (int i = 0; i < seats.length; i++) {
            String[] curRowAndCol = seats[i].split(",");
            int curRow = Integer.parseInt(curRowAndCol[0]);
            int curCol = Integer.parseInt(curRowAndCol[1]);

            if (curRow > roomRow || curRow < 1
                    || curCol > roomCol || curCol < 1) {
                return "Seat " + "(" + curRow + "," + curCol + ") does not exist in this room";
            }
            Optional<Booking> byId = bookingRepository.findById(new Booking.Key(bookingDto.getMovieName(),
                    bookingDto.getRoomName(), bookingDto.getDate(),
                    curRow, curCol));

            if (byId.isPresent()) {
                return "Seat " + "(" + curRow + "," + curCol + ") is already taken";
            }
            seatsAsReturnString.append("(").append(curRow).append(",").append(curCol).append("), ");
            approvedBookings.add(new Booking(bookingDto.getUser(), 1500,
                    bookingDto.getMovieName(), bookingDto.getRoomName(),
                    bookingDto.getDate(), curRow, curCol));

        }
        String valueAsString = String.valueOf(1500 * seats.length);
        String s = seatsAsReturnString.substring(0, seatsAsReturnString.length() - 2);
        bookingRepository.saveAll(approvedBookings);

        return s + "; the price for this booking is " + valueAsString + " HUF";
    }

    @Override
    public String listBookingsForUser(String user) {
        List<Screening> allScreenings = screeningService.findAll();
        if (bookingRepository.findByUser(user).size() == 0) {
            return "You have not booked any tickets yet";
        }
        String returnString = "Your previous bookings are\n";
        for (int i = 0; i < allScreenings.size(); i++) {
            Screening currentScreening = allScreenings.get(i);
            List<Booking> byUserAndScreening = bookingRepository
                    .findByUserAndScreening(user, allScreenings.get(i).getKey().getRoomName(),
                            allScreenings.get(i).getKey().getDate());

            if (byUserAndScreening.size() != 0) {
                String userScreeningString = "Seats ";
                for (int j = 0; j < byUserAndScreening.size(); j++) {
                    Booking currentBooking = byUserAndScreening.get(j);
                    int row = currentBooking.getKey().getRowNum();
                    int col = currentBooking.getKey().getColNum();
                    userScreeningString = userScreeningString + "(" + row + "," + col + "), ";
                }
                userScreeningString = userScreeningString.substring(0, userScreeningString.length() - 2);
                String value = String.valueOf(1500 * byUserAndScreening.size());
                returnString = returnString + userScreeningString + " on " + currentScreening.getKey().getMovieName()
                        + " in room " + currentScreening.getKey().getRoomName() + " starting at "
                        + currentScreening.getKey().getDate() + " for " + value + " HUF\n";
            }
        }
        return returnString.substring(0, returnString.length() - 1);
    }


}
