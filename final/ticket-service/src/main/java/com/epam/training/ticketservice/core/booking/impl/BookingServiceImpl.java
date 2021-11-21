package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.pricing.PricingService;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private int basePrice = 1500;
    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final ScreeningService screeningService;
    private final MovieService movieService;
    private final PricingService pricingService;


    public BookingServiceImpl(BookingRepository bookingRepository,
                              RoomService roomService,
                              ScreeningService screeningService, MovieService movieService,
                              PricingService pricingService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.screeningService = screeningService;
        this.movieService = movieService;
        this.pricingService = pricingService;
    }

    @Override
    public int getPriceForBooking(Screening screening) {
        int value = basePrice;
        String screeningPrice = screening.getPriceComponent();
        String roomName = screening.getKey().getRoomName();
        String movieName = screening.getKey().getMovieName();
        String roomPrice = roomService.getRoomByName(roomName).get().getPriceComponent();
        String moviePrice = movieService.getMovieByName(movieName).get().getPriceComponent();
        value = value + pricingService.getValuebyName(screeningPrice)
                + pricingService.getValuebyName(roomPrice)
                + pricingService.getValuebyName(moviePrice);
        return value;
    }


    @Override
    public String createBooking(BookingDto bookingDto) {
        Objects.requireNonNull(bookingDto, "Booking cannot be null");
        Objects.requireNonNull(bookingDto.getSeats(), "Seats cannot be null");
        Objects.requireNonNull(bookingDto.getDate(), "Date cannot be null");
        Objects.requireNonNull(bookingDto.getMovieName(), "Movie name cannot be null");
        Objects.requireNonNull(bookingDto.getRoomName(), "Room name cannot be null");
        Objects.requireNonNull(bookingDto.getUser(), "User cannot be null");


        Optional<Screening> screeningById = screeningService.findById(new Screening.Key(bookingDto.getMovieName(),
                bookingDto.getRoomName(), bookingDto.getDate()));

        if (screeningById.isEmpty()) {
            return "Screening does not exist";
        }

        int value = getPriceForBooking(screeningById.get());

        Room roomByName = roomService.getRoomByName(bookingDto.getRoomName()).get();


        int roomRow = roomByName.getNumRow();
        int roomCol = roomByName.getNumCol();

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
            approvedBookings.add(new Booking(bookingDto.getUser(), value,
                    bookingDto.getMovieName(), bookingDto.getRoomName(),
                    bookingDto.getDate(), curRow, curCol));

        }
        String valueAsString = String.valueOf(value * seats.length);
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
                int screeningPrice = 0;
                for (int j = 0; j < byUserAndScreening.size(); j++) {
                    Booking currentBooking = byUserAndScreening.get(j);
                    int row = currentBooking.getKey().getRowNum();
                    int col = currentBooking.getKey().getColNum();
                    userScreeningString = userScreeningString + "(" + row + "," + col + "), ";
                    screeningPrice = screeningPrice + currentBooking.getPrice();
                }
                userScreeningString = userScreeningString.substring(0, userScreeningString.length() - 2);


                String value = String.valueOf(screeningPrice);


                returnString = returnString + userScreeningString + " on " + currentScreening.getKey().getMovieName()
                        + " in room " + currentScreening.getKey().getRoomName() + " starting at "
                        + currentScreening.getKey().getDate() + " for " + value + " HUF\n";
            }
        }
        return returnString.substring(0, returnString.length() - 1);
    }

    @Override
    public void changePrice(int price) {
        this.basePrice = price;
    }


}
