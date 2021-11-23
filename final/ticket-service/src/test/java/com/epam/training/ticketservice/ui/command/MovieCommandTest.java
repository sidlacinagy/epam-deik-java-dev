package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MovieCommandTest {

    private final UserServiceImpl userService = mock(UserServiceImpl.class);
    private final MovieServiceImpl movieService = mock(MovieServiceImpl.class);

    private MovieCommand underTest = new MovieCommand(userService,movieService);

    @Test
    public void testCreateMovieShouldReturnMovieDto(){
        //Given
        MovieDto movieDto=new MovieDto("dummy","genre",6);

        //When
        underTest.createMovie("dummy","genre",6);

        //Then
        verify(movieService).createMovie(movieDto);

    }

    @Test
    public void testUpdateMovieShouldReturnMovieDto(){
        //Given
        MovieDto movieDto=new MovieDto("dummy","genre",6);

        //When
        underTest.updateMovie("dummy","genre",6);

        //Then
        verify(movieService).updateMovie(movieDto);
    }

    @Test
    public void testDeleteMovieShouldReturnDeletedMovie(){
        //Given-When-Then
        assertEquals(underTest.deleteMovie("dummy"),"Deleted dummy");
        verify(movieService).deleteMovie("dummy");

    }

    @Test
    public void testListMoviesShouldReturnMoviesAsCorrectStringWhenThereAreMovies(){
        //Given
        when(movieService.getMovieList()).thenReturn(List.of(new MovieDto("dummyMovie","genre1",2)
                ,new MovieDto("dummyMovie2","genre2",5)));
        String expected="dummyMovie (genre1, 2 minutes)\ndummyMovie2 (genre2, 5 minutes)";

        //When
        String result=underTest.listMovies();

        //Then
        assertEquals(expected,result);

    }

    @Test
    public void testListMoviesShouldReturnMoviesAsCorrectStringWhenThereAreNoMovies(){
        //Given
        when(movieService.getMovieList()).thenReturn(List.of());
        String expected="There are no movies at the moment";

        //When
        String result=underTest.listMovies();

        //Then
        assertEquals(expected,result);

    }
}
