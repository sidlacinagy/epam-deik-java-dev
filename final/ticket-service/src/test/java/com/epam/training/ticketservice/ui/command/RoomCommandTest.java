package com.epam.training.ticketservice.ui.command;


import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoomCommandTest {

    private final UserServiceImpl userService = mock(UserServiceImpl.class);
    private final RoomServiceImpl roomService = mock(RoomServiceImpl.class);

    private RoomCommand underTest = new RoomCommand(userService,roomService);

    @Test
    public void testCreateRoomShouldReturnRoomDto(){
        //Given
        RoomDto roomDto=new RoomDto("dummy",5,6);

        //When
        underTest.createRoom("dummy",5,6);

        //Then
        verify(roomService).createRoom(roomDto);

    }

    @Test
    public void testUpdateRoomShouldReturnRoomDto(){
        //Given
        RoomDto roomDto=new RoomDto("dummy",5,6);

        //When
        underTest.updateRoom("dummy",5,6);

        //Then
        verify(roomService).updateRoom(roomDto);
    }

    @Test
    public void testDeleteRoomShouldReturnDeletedRoom(){
        //Given-When-Then
        assertEquals(underTest.deleteRoom("dummy"),"Deleted room: dummy");
        verify(roomService).deleteRoom("dummy");

    }

    @Test
    public void testListRoomsShouldReturnRoomsAsCorrectStringWhenThereAreRooms(){
        //Given
        when(roomService.getRoomList()).thenReturn(List.of(new RoomDto("dummyRoom",1,2)
                ,new RoomDto("dummyRoom2",5,2)));
        String expected="Room dummyRoom with 2 seats, 1 rows and 2 columns\nRoom dummyRoom2 with 10 seats, 5 rows and 2 columns";

        //When
        String result=underTest.listRooms();

        //Then
        assertEquals(expected,result);

    }

    @Test
    public void testListRoomsShouldReturnRoomsAsCorrectStringWhenThereAreNoRooms(){
        //Given
        when(roomService.getRoomList()).thenReturn(List.of());
        String expected="There are no rooms at the moment";

        //When
        String result=underTest.listRooms();

        //Then
        assertEquals(expected,result);

    }


}
