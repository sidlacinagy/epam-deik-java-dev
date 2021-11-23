package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ScreeningCommandTest {

    private final UserServiceImpl userService=mock(UserServiceImpl.class);
    private final ScreeningServiceImpl screeningService=mock(ScreeningServiceImpl.class);
    private final MovieServiceImpl movieService=mock(MovieServiceImpl.class);

    private final ScreeningCommand underTest=new ScreeningCommand(userService,screeningService,movieService);

    @Test
    public void testListScreeningShouldReturnNoScreeningsWhenThereAreNoScreenings(){
        //Given
        when(screeningService.getScreeningList()).thenReturn(List.of());
        String expected="There are no screenings";

        //When
        String result=underTest.listScreening();

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testListScreeningShouldReturnScreeningsWhenThereAreScreenings(){
        //Given
        when(screeningService.getScreeningList()).thenReturn(List.of(new ScreeningDto("dummyMovie","dummyRoom","dummyDate"),
        new ScreeningDto("dummyMovie2","dummyRoom2","dummyDate2")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.of(new Movie("dummyMovie","genre",5)));
        when(movieService.getMovieByName("dummyMovie2")).thenReturn(Optional.of(new Movie("dummyMovie2","genre2",10)));
        String expected="dummyMovie (genre, 5 minutes), screened in room dummyRoom, " +
                "at dummyDate\ndummyMovie2 (genre2, 10 minutes), screened in room dummyRoom2, at dummyDate2";

        //When
        String result=underTest.listScreening();

        //Then
        assertEquals(expected,result);
    }




}
