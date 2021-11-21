package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class RoomCommand {

    private final UserService userService;
    private final RoomService roomService;

    public RoomCommand(UserService userService, RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create room", value = "Create new room")
    public RoomDto createRoom(String name, int row, int column) {
        RoomDto roomDto = RoomDto.builder()
                .withName(name)
                .withColumn(column)
                .withRow(row)
                .build();
        roomService.createRoom(roomDto);
        return roomDto;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update room", value = "Update movie")
    public String updateRoom(String name, int row, int column) {
        RoomDto roomDto = RoomDto.builder()
                .withName(name)
                .withColumn(column)
                .withRow(row)
                .build();
        return roomService.updateRoom(roomDto);
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete room", value = "Delete room")
    public String deleteMovie(String name) {
        roomService.deleteRoom(name);
        return "Deleted room: " + name;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to room", value = "attach price component to room")
    public String attackMovie(String priceName,String roomName) {
        return roomService.updatePriceComponent(priceName,roomName);
    }


    @ShellMethod(key = "list rooms", value = "List rooms")
    public String listMovies() {
        List<RoomDto> roomList = roomService.getRoomList();
        if (roomList.size() == 0) {
            return "There are no rooms at the moment";
        }
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < roomList.size(); i++) {
            str.append("Room ").append(roomList.get(i).getName())
                    .append(" with ").append(roomList.get(i).getRows() * roomList.get(i).getColumns())
                    .append(" seats, ").append(roomList.get(i).getRows()).append(" rows and ")
                    .append(roomList.get(i).getColumns()).append(" columns\n");
        }
        return str.substring(0, (str.length() - 1));
    }


    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }


}
