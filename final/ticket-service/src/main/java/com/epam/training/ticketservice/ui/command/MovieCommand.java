package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class MovieCommand {

    private final UserService userService;
    private final MovieService movieService;

    public MovieCommand(UserService userService, MovieService movieService) {
        this.userService = userService;
        this.movieService = movieService;
    }


    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Create new movie")
    public MovieDto createMovie(String name, String genre, int length) {
        MovieDto movieDto = MovieDto.builder()
                .withName(name)
                .withGenre(genre)
                .withLength(length)
                .build();
        movieService.createMovie(movieDto);
        return movieDto;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update movie", value = "Update movie")
    public String updateMovie(String name, String genre, int length) {
        MovieDto movieDto = MovieDto.builder()
                .withName(name)
                .withGenre(genre)
                .withLength(length)
                .build();
        return movieService.updateMovie(movieDto);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete movie", value = "Delete movie")
    public String deleteMovie(String name) {
        movieService.deleteMovie(name);
        return "Deleted " + name;
    }


    @ShellMethod(key = "list movies", value = "List movies")
    public String listMovies() {
        List<MovieDto> movieList = movieService.getMovieList();
        if (movieList.size() == 0) {
            return "There are no movies at the moment";
        }
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < movieList.size(); i++) {
            str.append(movieList.get(i).getName()).append(" (")
                    .append(movieList.get(i).getGenre()).append(", ")
                    .append(movieList.get(i).getLength()).append(" minutes)").append("\n");
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
