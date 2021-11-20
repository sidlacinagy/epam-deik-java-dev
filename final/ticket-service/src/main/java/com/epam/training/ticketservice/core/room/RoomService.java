package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    void createRoom(RoomDto roomDto);

    String updateRoom(RoomDto roomDto);

    void deleteRoom(String name);

    List<RoomDto> getRoomList();

    Optional<Room> getRoomByName(String name);
}
