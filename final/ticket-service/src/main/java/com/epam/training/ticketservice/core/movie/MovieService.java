package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    void createMovie(MovieDto movie);

    String updateMovie(MovieDto movie);

    void deleteMovie(String name);

    List<MovieDto> getMovieList();

    Optional<Movie> getMovieByName(String name);
}
