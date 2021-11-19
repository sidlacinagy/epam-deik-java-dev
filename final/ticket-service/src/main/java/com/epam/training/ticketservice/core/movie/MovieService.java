package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;

import java.util.List;

public interface MovieService {

    void createMovie(MovieDto movie);

    String updateMovie(MovieDto movie);

    void deleteMovie(String name);

    List<MovieDto> getMovieList();

}
