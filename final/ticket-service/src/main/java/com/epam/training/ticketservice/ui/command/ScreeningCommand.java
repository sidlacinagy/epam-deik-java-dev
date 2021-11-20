package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
public class ScreeningCommand {
    private final UserService userService;
    private final ScreeningService screeningService;
    private final MovieService movieService;


    public ScreeningCommand(UserService userService, ScreeningService screeningService, MovieService movieService) {
        this.userService = userService;
        this.screeningService = screeningService;
        this.movieService = movieService;
    }


    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Create new screening")
    public String createScreening(String movieName, String roomName, String date) {
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovieName(movieName)
                .withRoomName(roomName)
                .withDate(date)
                .build();
        return screeningService.createScreening(screeningDto);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Delete screening")
    public String deleteScreening(String movieName, String roomName, String date) {
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovieName(movieName)
                .withRoomName(roomName)
                .withDate(date)
                .build();
        screeningService.deleteScreening(screeningDto);
        return "Successfully deleted screening";
    }


    @ShellMethod(key = "list screenings", value = "List screening")
    public String listScreening() {
        List<ScreeningDto> screeningList = screeningService.getScreeningList();
        if (screeningList.size() == 0) {
            return "There are no screenings";
        }
        StringBuilder str = new StringBuilder();

        Collator coll = Collator.getInstance(new Locale("hu", "HU"));

        screeningList = screeningList.stream()
                .sorted(Comparator.comparing(ScreeningDto::getMovieName, coll))
                .collect(Collectors.toList());

        for (int i = 0; i < screeningList.size(); i++) {
            ScreeningDto currentScreening = screeningList.get(i);
            Optional<Movie> movieByName = movieService.getMovieByName(currentScreening.getMovieName());
            if (movieByName.isEmpty()) {
                return "Database is asynchronous";
            }
            String genre = movieByName.get().getGenre();
            int length = movieByName.get().getLength();
            str.append(currentScreening.getMovieName()).append(" (").append(genre).append(", ").append(length)
                    .append(" minutes), screened in room ").append(currentScreening.getRoomName()).append(", at ")
                    .append(currentScreening.getDate()).append("\n");
        }
        return str.substring(0, (str.length() - 1));
    }


    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }


}
