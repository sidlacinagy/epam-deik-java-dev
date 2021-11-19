package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class InMemoryDatabaseInitializer {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public InMemoryDatabaseInitializer(MovieRepository productRepository, UserRepository userRepository) {
        this.movieRepository = productRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Movie hete = new Movie("hete","hhheee",5);
        Movie koli = new Movie("koli","geko",15);
        movieRepository.saveAll(List.of(hete,koli));

        User admin = new User("admin", "admin", User.Role.ADMIN);
        userRepository.save(admin);
    }

}
