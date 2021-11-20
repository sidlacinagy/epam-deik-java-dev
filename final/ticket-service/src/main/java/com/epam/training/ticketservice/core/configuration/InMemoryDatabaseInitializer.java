package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class InMemoryDatabaseInitializer {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;

    public InMemoryDatabaseInitializer(MovieRepository productRepository,
                                       UserRepository userRepository, RoomRepository roomRepository,
                                       ScreeningRepository screeningRepository) {
        this.movieRepository = productRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.screeningRepository = screeningRepository;
    }

    @PostConstruct
    public void init() {

        User admin = new User("admin", "admin", User.Role.ADMIN);
        userRepository.save(admin);

    }

}
