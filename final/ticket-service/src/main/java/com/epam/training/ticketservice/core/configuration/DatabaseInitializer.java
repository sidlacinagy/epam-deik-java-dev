package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {


    private final UserRepository userRepository;


    public DatabaseInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {

        User admin = new User("admin", "admin", User.Role.ADMIN);
        userRepository.save(admin);


    }

}
