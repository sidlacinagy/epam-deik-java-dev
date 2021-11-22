package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.pricing.impl.PricingServiceImpl;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RoomServiceImplTest {
    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final PricingServiceImpl pricingService = mock(PricingServiceImpl.class);
    private RoomService underTest = new RoomServiceImpl(roomRepository, pricingService);


    @Test
    public void testGetRoomByNameShouldReturnCorrectRoomWhenRoomIdExists() {
        // Given
        when(roomRepository.findById("dummy")).thenReturn(Optional.of(new Room("dummy", 5,6,"Price")));

        //When
        Optional<Room> result=underTest.getRoomByName("dummy");

        //Then
        assertEquals(Optional.of(new Room("dummy", 5,6,"Price")),result);
    }

    @Test
    public void testGetRoomByNameShouldReturnCorrectRoomWhenRoomIdDoesntExist() {
        // Given-When
        Optional<Room> expected= Optional.empty();
        Optional<Room> result=underTest.getRoomByName("dummy");

        //Then
        assertEquals(expected,result);
    }


    @Test
    public void testUpdateRoomShouldThrowNullPointerExceptionWhenRoomDtoIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.updateRoom(null));
    }

    @Test
    public void testUpdateRoomShouldReturnSuccessfulWhenRoomDtoExistsAndRoomExists() {
        // Given
        RoomDto roomDto=new RoomDto("dummy",5,5);
        when(roomRepository.findById("dummy")).thenReturn(Optional.of(new Room("dummy",5,5,"dum")));

        //When
        String result = underTest.updateRoom(roomDto);

        //Then
        assertEquals("Successfully updated",result);
    }

    @Test
    public void testUpdateRoomShouldReturnFailWhenRoomDtoExistsAndRoomNotExists() {

        //Given
        RoomDto roomDto=new RoomDto("dummy",5,5);

        //When
        String result = underTest.updateRoom(roomDto);

        //Then
        assertEquals("Could not update",result);
    }

    @Test
    public void testCreateRoomShouldCallWhenRoomDtoExists() {
        // Given
        RoomDto roomDto=new RoomDto("dummy",5,5);
        Room room=new Room("dummy",5,5);

        //When
        underTest.createRoom(roomDto);

        //Then
        verify(roomRepository).save(room);
    }

    @Test
    public void testUpdatePriceComponentShouldReturnSuccessWhenPricingAndRoomExist() {
        // Given
        when(roomRepository.findById("dummy")).thenReturn(Optional.of(new Room("dummy",5,5,"")));
        when(pricingService.doesPricingExist("PriceDummy")).thenReturn(true);

        //When
        String result=underTest.updatePriceComponent("PriceDummy","dummy");

        //Then
        assertEquals("Successfully updated",result);
    }

    @Test
    public void testUpdatePriceComponentShouldReturnFailWhenPricingExistsAndRoomDoesnt() {
        // Given
        when(pricingService.doesPricingExist("PriceDummy")).thenReturn(true);

        //When
        String result=underTest.updatePriceComponent("PriceDummy","Dune");

        //Then
        assertEquals("Could not update",result);
    }

    @Test
    public void testUpdatePriceComponentShouldReturnFailWhenPricingDoesntExistAndRoomDoes() {
        // Given
        when(roomRepository.findById("dummy")).thenReturn(Optional.of(new Room("dummy",5,5,"")));


        //When
        String result=underTest.updatePriceComponent("PriceDummy","dummy");

        //Then
        assertEquals("Pricing does not exist",result);
    }

    @Test
    public void testDeleteRoomShouldCallWhenNameExists() {
        // Given
        String roomNameDummy="dummy";

        //When
        underTest.deleteRoom("dummy");

        //Then
        verify(roomRepository).deleteById("dummy");
    }

    @Test
    public void testGetRoomListShouldReturnCorrectRoomDtos() {
        // Given
        when(roomRepository.findAll()).thenReturn(List.of((new Room("dummy",5,6))
                ,new Room("dummy2",7,8)));
        List<RoomDto> expected=List.of(new RoomDto("dummy",5,6),
                new RoomDto("dummy2",7,8));

        //When
        List<RoomDto> result = underTest.getRoomList();

        //Then
        assertEquals(expected,result);
    }

}
