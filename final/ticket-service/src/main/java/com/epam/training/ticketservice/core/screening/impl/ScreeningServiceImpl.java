package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private RoomService roomService;
    private MovieService movieService;

    private final ScreeningRepository screeningRepository;

    public ScreeningServiceImpl(RoomService roomService, MovieService movieService, ScreeningRepository screeningRepository) {
        this.roomService = roomService;
        this.movieService = movieService;
        this.screeningRepository = screeningRepository;
    }



    @Override
    public String createScreening(ScreeningDto screeningDto) {

        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getDate(), "Date cannot be null");
        Objects.requireNonNull(screeningDto.getMovieName(), "Movie name cannot be null");
        Objects.requireNonNull(screeningDto.getRoomName(), "Room name cannot be null");

        Optional<Room> room = roomService.getRoomByName(screeningDto.getRoomName());

        if(room.isEmpty()){
            return "Room does not exist";
        }
        Optional<Movie> movie = movieService.getMovieByName(screeningDto.getMovieName());
        if(movie.isEmpty()) {
            return "Movie does not exist";
        }

        List<Screening> byRoom = screeningRepository.findByRoom(room.get().getName());

        int length=movieService.getMovieByName(movie.get().getName()).get().getLength();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(screeningDto.getDate(), formatter);
        LocalDateTime endDate=startDate.plusMinutes(length);


        for(int i=0;i<byRoom.size();i++){
            Screening currentScreening=byRoom.get(i);
            String currentDateAsString=currentScreening.getKey().getDate();
            LocalDateTime currentDate = LocalDateTime.parse(currentDateAsString, formatter);
            String currentMovie=currentScreening.getKey().getMovieName();
            Optional<Movie> currentMovie1 = movieService.getMovieByName(currentMovie);

            if(movie.isEmpty()){
                return "Database is asynchronous";
            }
            int currentLength=currentMovie1.get().getLength();
            LocalDateTime currentFinishDate=currentDate.plusMinutes(currentLength);


            if((startDate.isBefore(currentFinishDate.plusMinutes(1)) && startDate.isAfter(currentDate.minusMinutes(1))) ||
                    (endDate.isBefore(currentFinishDate.plusMinutes(1)) && endDate.isAfter(currentDate.minusMinutes(1)))) {
                return "There is an overlapping screening";
            }


        }

        for(int i=0;i<byRoom.size();i++){
            Screening currentScreening=byRoom.get(i);
            String currentDateAsString=currentScreening.getKey().getDate();
            LocalDateTime currentDate = LocalDateTime.parse(currentDateAsString, formatter);
            String currentMovie=currentScreening.getKey().getMovieName();
            Optional<Movie> currentMovie1 = movieService.getMovieByName(currentMovie);

            if(movie.isEmpty()){
                return "Database is asynchronous";
            }
            int currentLength=currentMovie1.get().getLength();
            LocalDateTime currentFinishDateWithBreak=currentDate.plusMinutes(currentLength+10);

            if((startDate.isBefore(currentFinishDateWithBreak.plusMinutes(1)) && startDate.isAfter(currentDate.minusMinutes(1))))
            {
                return "This would start in the break period after another screening in this room";
            }

        }
        Screening screening = new Screening(screeningDto.getMovieName(),screeningDto.getRoomName(),screeningDto.getDate());
        screeningRepository.save(screening);
        return "Successfully created screening";
    }


    @Override
    public void deleteScreening(ScreeningDto screeningDto) {
        screeningRepository.deleteById(new Screening.Key(screeningDto.getMovieName(),screeningDto.getRoomName(),screeningDto.getDate()));
    }

    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private ScreeningDto convertEntityToDto(Screening screening) {
        return ScreeningDto.builder()
                .withDate(screening.getKey().getDate())
                .withMovieName(screening.getKey().getMovieName())
                .withRoomName(screening.getKey().getRoomName())
                .build();
    }

    private Optional<ScreeningDto> convertEntityToDto(Optional<Screening> screening) {
        return screening.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(screening.get()));
    }
}
