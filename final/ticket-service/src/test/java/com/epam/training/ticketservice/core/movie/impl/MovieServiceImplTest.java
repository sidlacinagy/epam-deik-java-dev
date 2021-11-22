package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.pricing.PricingService;
import com.epam.training.ticketservice.core.pricing.impl.PricingServiceImpl;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class MovieServiceImplTest {

    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final PricingServiceImpl pricingService = mock(PricingServiceImpl.class);
    private MovieService underTest = new MovieServiceImpl(movieRepository, pricingService);


    @Test
    public void testGetRoomByNameShouldReturnCorrectRoomWhenRoomIdExists() {
        // Given
        when(movieRepository.findById("dummy")).thenReturn(Optional.of(new Movie("dummy", "genre",122)));

        //When
        Optional<Movie> result=underTest.getMovieByName("dummy");

        //Then
        assertEquals(Optional.of(new Movie("dummy", "genre",122)),result);
    }

    @Test
    public void testGetRoomByNameShouldReturnCorrectRoomWhenRoomIdDoesntExist() {
        // Given-When
        Optional<Movie> expected= Optional.empty();
        Optional<Movie> result=underTest.getMovieByName("dummy");

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testUpdateMovieShouldThrowNullPointerExceptionWhenMovieDtoIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.updateMovie(null));
    }

    @Test
    public void testUpdateMovieShouldReturnSuccessfulWhenMovieDtoExistsAndMovieExists() {
        // Given
        MovieDto movieDto=new MovieDto("Dune","Sci-fi",150);
        when(movieRepository.findById("Dune")).thenReturn(Optional.of(new Movie("Dune","Sci-fi",150)));

        //When
        String result = underTest.updateMovie(movieDto);

        //Then
        assertEquals("Successfully updated",result);
    }

    @Test
    public void testUpdateMovieShouldReturnFailWhenMovieDtoExistsAndMovieNotExists() {
        // Given
        MovieDto movieDto=new MovieDto("Dune12","Sci-fi",150);

        //When
        String result = underTest.updateMovie(movieDto);

        //Then
        assertEquals("Could not update",result);
    }

    @Test
    public void testCreateMovieShouldCallWhenMovieDtoExists() {
        // Given
        MovieDto movieDto=new MovieDto("Dune12365","Sci-fi",150);
        Movie movie= new Movie("Dune12365","Sci-fi",150);

        //When
        underTest.createMovie(movieDto);

        //Then
        verify(movieRepository).save(movie);
    }

    @Test
    public void testUpdatePriceComponentShouldReturnSuccessWhenPricingAndMovieExist() {
        // Given
        when(movieRepository.findById("Dune")).thenReturn(Optional.of(new Movie("Dune","Sci-fi",150)));
        when(pricingService.doesPricingExist("PriceDummy")).thenReturn(true);

        //When
        String result=underTest.updatePriceComponent("PriceDummy","Dune");

        //Then
        assertEquals("Successfully updated",result);
    }

    @Test
    public void testUpdatePriceComponentShouldReturnFailWhenPricingExistsAndMovieDoesnt() {
        // Given
        when(pricingService.doesPricingExist("PriceDummy")).thenReturn(true);

        //When
        String result=underTest.updatePriceComponent("PriceDummy","Dune");

        //Then
        assertEquals("Could not update",result);
    }

    @Test
    public void testUpdatePriceComponentShouldReturnFailWhenPricingDoesntExistAndMovieDoes() {
        // Given
        when(movieRepository.findById("Dune")).thenReturn(Optional.of(new Movie("Dune","Sci-fi",150)));


        //When
        String result=underTest.updatePriceComponent("PriceDummy","Dune");

        //Then
        assertEquals("Pricing does not exist",result);
    }

    @Test
    public void testDeleteMovieShouldCallWhenNameExists() {
        // Given
        String movieNameDummy="Dune";

        //When
        underTest.deleteMovie("Dune");

        //Then
        verify(movieRepository).deleteById("Dune");
    }

    @Test
    public void testGetMovieListShouldReturnCorrectMovieDtos() {
        // Given
        when(movieRepository.findAll()).thenReturn(List.of((new Movie("Dune","Sci-fi",150))
                ,new Movie("PlayBoyCarti","BreakTheBank",12)));
        List<MovieDto> expected=List.of(new MovieDto("Dune","Sci-fi",150),
                new MovieDto("PlayBoyCarti","BreakTheBank",12));

        //When
        List<MovieDto> result = underTest.getMovieList();

        //Then
        assertEquals(expected,result);
    }





}
