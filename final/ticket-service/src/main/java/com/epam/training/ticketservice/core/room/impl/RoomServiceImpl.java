package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;


    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Optional<Room> getRoomByName(String name) {
        return roomRepository.findById(name);
    }

    @Override
    public void createRoom(RoomDto roomDto) {
        Objects.requireNonNull(roomDto, "Room cannot be null");
        Objects.requireNonNull(roomDto.getRows(), "Row cannot be null");
        Objects.requireNonNull(roomDto.getColumns(), "Col cannot be null");
        Objects.requireNonNull(roomDto.getName(), "Name cannot be null");
        Room room = new Room(roomDto.getName(), roomDto.getRows(), roomDto.getColumns());
        roomRepository.save(room);
    }

    @Override
    public String updateRoom(RoomDto roomDto) {
        Objects.requireNonNull(roomDto, "Room cannot be null");
        Objects.requireNonNull(roomDto.getRows(), "Row cannot be null");
        Objects.requireNonNull(roomDto.getColumns(), "Col cannot be null");
        Objects.requireNonNull(roomDto.getName(), "Name cannot be null");
        Optional<Room> room = roomRepository.findById(roomDto.getName());

        if (room.isPresent()) {
            Room room1 = room.get();
            room1.setNumCol(roomDto.getColumns());
            room1.setNumRow(roomDto.getRows());
            roomRepository.save(room1);
            return "Successfully updated";
        }
        return "Could not update";
    }

    @Override
    public void deleteRoom(String name) {
        roomRepository.deleteById(name);
    }

    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private RoomDto convertEntityToDto(Room room) {
        return RoomDto.builder()
                .withName(room.getName())
                .withRow(room.getNumRow())
                .withColumn(room.getNumCol())
                .build();
    }

    private Optional<RoomDto> convertEntityToDto(Optional<Room> room) {
        return room.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(room.get()));
    }
}
