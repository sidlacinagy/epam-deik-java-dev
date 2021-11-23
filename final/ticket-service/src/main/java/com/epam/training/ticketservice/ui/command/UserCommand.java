package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class UserCommand {

    private final UserService userService;
    private final BookingService bookingService;
    private final ScreeningService screeningService;

    public UserCommand(UserService userService, BookingService bookingService, ScreeningService screeningService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.screeningService = screeningService;
    }

    @ShellMethod(key = "sign in", value = "User login")
    public String loginUser(String username, String password) {
        Optional<UserDto> user = userService.login(username, password);
        if (user.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        if (user.get().getRole() == User.Role.USER) {
            return username + " is logged in!";
        }
        user = userService.logout();
        return "Login failed due to incorrect credentials";

    }

    @ShellMethod(key = "sign in privileged", value = "User login")
    public String loginAdmin(String username, String password) {
        Optional<UserDto> user = userService.login(username, password);
        if (user.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        if (user.get().getRole() == User.Role.ADMIN) {
            return username + " is logged in!";
        }
        user = userService.logout();
        return "Login failed due to incorrect credentials";
    }

    @ShellMethod(key = "sign out", value = "User logout")
    public String logout() {
        Optional<UserDto> user = userService.logout();
        if (user.isEmpty()) {
            return "You are not signed in";
        }
        return user.get().getUsername() + " is logged out!";
    }

    @ShellMethod(key = "show price for", value = "show price for a screening")
    public String show(String movieName, String roomName, String date, String seats) {
        Optional<Screening> byId = screeningService.findById(new Screening.Key(movieName, roomName, date));
        if (byId.isEmpty()) {
            return "No such screening";
        }
        String[] seatsSplit = seats.split(" ");
        int value = bookingService.getPriceForBooking(byId.get());
        return "The price for this booking would be " + value * seatsSplit.length + " HUF";
    }


    @ShellMethod(key = "describe account", value = "Get user information")
    public String printLoggedInUser() {
        Optional<UserDto> userDto = userService.getLoggedInUser();
        if (userDto.isEmpty()) {
            return "You are not signed in";
        }
        if (userDto.get().getRole() == User.Role.ADMIN) {
            return "Signed in with privileged account '" + userDto.get().getUsername() + "'";
        } else if (userDto.get().getRole() == User.Role.USER) {
            return "Signed in with account '"
                    + userDto.get().getUsername() + "'\n"
                    + bookingService.listBookingsForUser(userDto.get().getUsername());
        }
        return userDto.get().toString();
    }

    @ShellMethod(key = "sign up", value = "User registration")
    public String registerUser(String userName, String password) {
        try {
            userService.registerUser(userName, password);
            return "Registration was successful!";
        } catch (Exception e) {
            return "Registration failed!";
        }
    }

    @ShellMethod(key = "book", value = "Book for a screening")
    @ShellMethodAvailability("isAvailableForUser")
    public String bookForScreening(String movieName, String roomName, String date, String seats) {

        Optional<UserDto> user = userService.getLoggedInUser();
        BookingDto bookingDto = BookingDto.builder().date(date).roomName(roomName)
                .user(user.get().getUsername())
                .movieName(movieName).seats(seats).build();

        return bookingService.createBooking(bookingDto);

    }


    private Availability isAvailableForUser() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.USER) {
            return Availability.available();
        }
        return Availability.unavailable("You are not logged in as a User!");
    }


}
