package com.epam.training.ticketservice.core.room.model;

import lombok.Data;

@Data
public class RoomDto {

    private final String name;
    private final int rows;
    private final int columns;

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private String name;
        private int rows;
        private int columns;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withRow(int row) {
            this.rows = row;
            return this;
        }

        public Builder withColumn(int column) {
            this.columns = column;
            return this;
        }

        public RoomDto build() {
            return new RoomDto(name, rows, columns);
        }
    }
}
