package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import lombok.Data;

@Data
public class ScreeningDto {

    private final String movieName;
    private final String roomName;
    private final String date;

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private String movieName;
        private String roomName;
        private String date;

        public Builder withMovieName(String movieName) {
            this.movieName = movieName;
            return this;
        }

        public Builder withRoomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder withDate(String date) {
            this.date = date;
            return this;
        }

        public ScreeningDto build() {
            return new ScreeningDto(movieName, roomName, date);
        }
    }
}
