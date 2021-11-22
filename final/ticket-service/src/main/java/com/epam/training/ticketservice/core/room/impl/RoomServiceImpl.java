package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.pricing.PricingService;
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
    private PricingService pricingService;

    public RoomServiceImpl(RoomRepository roomRepository, PricingService pricingService) {
        this.roomRepository = roomRepository;
        this.pricingService = pricingService;
    }

    @Override
    public Optional<Room> getRoomByName(String name) {
        return roomRepository.findById(name);
    }

    @Override
    public void createRoom(RoomDto roomDto) {
        Room room = new Room(roomDto.getName(), roomDto.getRows(), roomDto.getColumns());
        roomRepository.save(room);
    }

    @Override
    public String updateRoom(RoomDto roomDto) {
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

    @Override
    public String updatePriceComponent(String priceName, String roomName) {
        if (!pricingService.doesPricingExist(priceName)) {
            return "Pricing does not exist";
        }

        Optional<Room> room = roomRepository.findById(roomName);
        if (room.isPresent()) {
            Room room1 = room.get();
            room1.setPriceComponent(priceName);
            roomRepository.save(room1);
            return "Successfully updated";
        }
        return "Could not update";

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
