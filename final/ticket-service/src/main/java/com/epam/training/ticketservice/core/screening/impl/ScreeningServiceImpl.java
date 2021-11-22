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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private RoomService roomService;
    private MovieService movieService;
    private PricingService pricingService;

    private final ScreeningRepository screeningRepository;

    public ScreeningServiceImpl(RoomService roomService,
                                MovieService movieService,
                                PricingService pricingService, ScreeningRepository screeningRepository) {
        this.roomService = roomService;
        this.movieService = movieService;
        this.pricingService = pricingService;
        this.screeningRepository = screeningRepository;
    }

    @Override
    public String updatePriceComponent(String priceName, Screening.Key key) {

        Optional<Screening> screening = screeningRepository.findById(key);

        if (!pricingService.doesPricingExist(priceName)) {
            return "Pricing does not exist";
        }

        if (screening.isPresent()) {
            Screening screening1 = screening.get();
            screening1.setPriceComponent(priceName);
            screeningRepository.save(screening1);
            return "Successfully updated";
        }
        return "Could not update";

    }


    @Override
    public String createScreening(ScreeningDto screeningDto) {


        Optional<Room> room = roomService.getRoomByName(screeningDto.getRoomName());

        if (room.isEmpty()) {
            return "Room does not exist";
        }
        Optional<Movie> movie = movieService.getMovieByName(screeningDto.getMovieName());
        if (movie.isEmpty()) {
            return "Movie does not exist";
        }

        List<Screening> byRoom = screeningRepository.findByRoom(room.get().getName());

        int length = movieService.getMovieByName(movie.get().getName()).get().getLength();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(screeningDto.getDate(), formatter);
        LocalDateTime endDate = startDate.plusMinutes(length);

        if (isOverLappingWithTimeInterval(byRoom, startDate, endDate, 0)) {
            return "There is an overlapping screening";
        }

        if (isOverLappingWithTimeInterval(byRoom, startDate, endDate, 10)) {
            return "This would start in the break period after another screening in this room";
        }

        Screening screening = new Screening(screeningDto.getMovieName(),
                screeningDto.getRoomName(),
                screeningDto.getDate());
        screeningRepository.save(screening);
        return "Successfully created screening";
    }


    @Override
    public void deleteScreening(ScreeningDto screeningDto) {
        screeningRepository.deleteById(new Screening.Key(screeningDto.getMovieName(),
                screeningDto.getRoomName(),
                screeningDto.getDate()));
    }

    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public List<Screening> findAll() {
        return screeningRepository.findAll();
    }

    @Override
    public Optional<Screening> findById(Screening.Key key) {
        return screeningRepository.findById(key);
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

    private boolean isOverLappingWithTimeInterval(List<Screening> byRoom,
                                                  LocalDateTime startDate,
                                                  LocalDateTime endDate,
                                                  int interval) {
        for (int i = 0; i < byRoom.size(); i++) {
            Screening currentScreening = byRoom.get(i);
            String currentDateAsString = currentScreening.getKey().getDate();
            LocalDateTime currentDate = LocalDateTime
                    .parse(currentDateAsString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String currentMovie = currentScreening.getKey().getMovieName();
            int currentLength = movieService.getMovieByName(currentMovie).get().getLength();
            LocalDateTime currentFinishDate = currentDate.plusMinutes(currentLength + 1 + interval);
            currentDate = currentDate.minusMinutes(1 + interval);


            if ((startDate.isBefore(currentFinishDate) && startDate.isAfter(currentDate))
                    || (endDate.isBefore(currentFinishDate) && endDate.isAfter(currentDate))) {
                return true;
            }
        }
        return false;
    }
}
