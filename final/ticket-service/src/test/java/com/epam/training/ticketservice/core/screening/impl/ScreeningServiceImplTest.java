package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.pricing.PricingService;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScreeningServiceImplTest {

    private RoomService roomService=mock(RoomService.class);
    private MovieService movieService=mock(MovieService.class);
    private PricingService pricingService=mock(PricingService.class);
    private final ScreeningRepository screeningRepository=mock(ScreeningRepository.class);

    private ScreeningService underTest = new ScreeningServiceImpl(roomService,movieService,pricingService,screeningRepository);

    @Test
    public void testUpdatePriceComponentShouldReturnSuccessWhenPricingAndScreeningExist() {
        // Given
        when(screeningRepository.findById(new Screening.Key("dummy","dummy1","dummydate")))
                .thenReturn(Optional.of(new Screening("dummy","dummy1","dummydate","PriceDummy")));
        when(pricingService.doesPricingExist("PriceDummy")).thenReturn(true);

        //When
        String result=underTest.updatePriceComponent("PriceDummy",new Screening.Key("dummy","dummy1","dummydate"));

        //Then
        assertEquals("Successfully updated",result);
    }

    @Test
    public void testUpdatePriceComponentShouldReturnFailWhenPricingExistsAndScreeningDoesnt() {
        // Given
        when(pricingService.doesPricingExist("PriceDummy")).thenReturn(true);

        //When
        String result=underTest.updatePriceComponent("PriceDummy",new Screening.Key("dummy","dummy1","dummydate"));

        //Then
        assertEquals("Could not update",result);
    }

    @Test
    public void testUpdatePriceComponentShouldReturnFailWhenPricingDoesntExistAndScreeningDoes() {
        // Given
        when(screeningRepository.findById(new Screening.Key("dummy","dummy1","dummydate")))
                .thenReturn(Optional.of(new Screening("dummy","dummy1","dummydate","PriceDummy")));

        //When
        String result=underTest.updatePriceComponent("PriceDummy",new Screening.Key("dummy","dummy1","dummydate"));

        //Then
        assertEquals("Pricing does not exist",result);
    }

    @Test
    public void testCreateScreeningShouldReturnFailWhenRoomDoesntExist(){
        //Given
        ScreeningDto screeningDto=new ScreeningDto("dummyMovie","dummyRoom","dummyDate");
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.empty());

        //When
        String result = underTest.createScreening(screeningDto);

        //Then
        assertEquals("Room does not exist",result);
    }

    @Test
    public void testCreateScreeningShouldReturnFailWhenMovieDoesntExist(){
        //Given
        ScreeningDto screeningDto=new ScreeningDto("dummyMovie","dummyRoom","dummyDate");
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.of(new Room("dummyRoom",5,6,"priceDummy")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.empty());

        //When
        String result = underTest.createScreening(screeningDto);

        //Then
        assertEquals("Movie does not exist",result);
    }

    @Test
    public void testCreateScreeningShouldReturnFailWhenScreeningWouldOverlapEnd(){
        //Given
        ScreeningDto screeningDto=new ScreeningDto("dummyMovie","dummyRoom","2021-03-15 11:00");
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.of(new Room("dummyRoom",5,6,"priceDummy")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.of(new Movie("dummyMovie","genre",120)));
        when(screeningRepository.findByRoom("dummyRoom"))
                .thenReturn(List.of(new Screening("dummyMovie","dummyRoom","2021-03-15 09:10")));
        //When
        String result = underTest.createScreening(screeningDto);

        //Then
        assertEquals("There is an overlapping screening",result);
    }

    @Test
    public void testCreateScreeningShouldReturnFailWhenScreeningWouldOverlapStart(){
        //Given
        ScreeningDto screeningDto=new ScreeningDto("dummyMovie","dummyRoom","2021-03-15 11:00");
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.of(new Room("dummyRoom",5,6,"priceDummy")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.of(new Movie("dummyMovie","genre",120)));
        when(screeningRepository.findByRoom("dummyRoom"))
                .thenReturn(List.of(new Screening("dummyMovie","dummyRoom","2021-03-15 12:50")));
        //When
        String result = underTest.createScreening(screeningDto);

        //Then
        assertEquals("There is an overlapping screening",result);
    }

    @Test
    public void testCreateScreeningShouldReturnFailWhenScreeningWouldOverlapBreakEnd(){
        //Given
        ScreeningDto screeningDto=new ScreeningDto("dummyMovie","dummyRoom","2021-03-15 11:00");
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.of(new Room("dummyRoom",5,6,"priceDummy")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.of(new Movie("dummyMovie","genre",120)));
        when(screeningRepository.findByRoom("dummyRoom"))
                .thenReturn(List.of(new Screening("dummyMovie","dummyRoom","2021-03-15 08:55")));
        //When
        String result = underTest.createScreening(screeningDto);

        //Then
        assertEquals("This would start in the break period after another screening in this room",result);
    }

    @Test
    public void testCreateScreeningShouldReturnFailWhenScreeningWouldOverlapBreakStart(){
        //Given
        ScreeningDto screeningDto=new ScreeningDto("dummyMovie","dummyRoom","2021-03-15 11:00");
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.of(new Room("dummyRoom",5,6,"priceDummy")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.of(new Movie("dummyMovie","genre",120)));
        when(screeningRepository.findByRoom("dummyRoom"))
                .thenReturn(List.of(new Screening("dummyMovie","dummyRoom","2021-03-15 13:05")));
        //When
        String result = underTest.createScreening(screeningDto);

        //Then
        assertEquals("This would start in the break period after another screening in this room",result);
    }

    @Test
    public void testCreateScreeningShouldReturnSuccess(){
        //Given
        ScreeningDto screeningDto=new ScreeningDto("dummyMovie","dummyRoom","2021-03-15 11:00");
        when(roomService.getRoomByName("dummyRoom")).thenReturn(Optional.of(new Room("dummyRoom",5,6,"priceDummy")));
        when(movieService.getMovieByName("dummyMovie")).thenReturn(Optional.of(new Movie("dummyMovie","genre",120)));
        when(screeningRepository.findByRoom("dummyRoom"))
                .thenReturn(List.of(new Screening("dummyMovie","dummyRoom","2021-03-15 13:15")));
        //When
        String result = underTest.createScreening(screeningDto);

        //Then
        assertEquals("Successfully created screening",result);
    }




}
