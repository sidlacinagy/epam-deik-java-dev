package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.pricing.PricingService;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final PricingService pricingService;

    public MovieServiceImpl(MovieRepository movieRepository, PricingService pricingService) {
        this.movieRepository = movieRepository;
        this.pricingService = pricingService;
    }

    @Override
    public Optional<Movie> getMovieByName(String name) {
        return movieRepository.findById(name);
    }

    @Override
    public String updatePriceComponent(String priceName, String movieName) {

        if (!pricingService.doesPricingExist(priceName)) {
            return "Pricing does not exist";
        }
        Optional<Movie> movie = movieRepository.findById(movieName);
        if (movie.isPresent()) {
            Movie movie1 = movie.get();
            movie1.setPriceComponent(priceName);
            movieRepository.save(movie1);
            return "Successfully updated";
        }
        return "Could not update";

    }

    @Override
    public void createMovie(MovieDto movieDto) {
        Movie movie = new Movie(movieDto.getName(), movieDto.getGenre(), movieDto.getLength());
        movieRepository.save(movie);
    }

    @Override
    public String updateMovie(MovieDto movieDto) {
        Optional<Movie> movie = movieRepository.findById(movieDto.getName());

        if (movie.isPresent()) {
            Movie movie1 = movie.get();
            movie1.setGenre(movieDto.getGenre());
            movie1.setLength(movieDto.getLength());
            movieRepository.save(movie1);
            return "Successfully updated";
        }
        return "Could not update";

    }

    @Override
    public void deleteMovie(String name) {
        movieRepository.deleteById(name);
    }

    @Override
    public List<MovieDto> getMovieList() {
        return movieRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private MovieDto convertEntityToDto(Movie movie) {
        return MovieDto.builder()
                .withName(movie.getName())
                .withGenre(movie.getGenre())
                .withLength(movie.getLength())
                .build();
    }

    private Optional<MovieDto> convertEntityToDto(Optional<Movie> movie) {
        return movie.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(movie.get()));
    }
}
