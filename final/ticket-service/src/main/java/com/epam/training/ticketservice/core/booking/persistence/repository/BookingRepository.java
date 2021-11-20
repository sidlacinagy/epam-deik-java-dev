package com.epam.training.ticketservice.core.booking.persistence.repository;

import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Booking.Key> {
    @Query("SELECT r FROM Booking r WHERE r.user = :user")
    List<Booking> findByUser(@Param("user") String user);

    @Query("SELECT r FROM Booking r WHERE r.user = :user and r.key.roomName=:room and r.key.date=:date")
    List<Booking> findByUserAndScreening(@Param("user") String user,
                                         @Param("room") String room, @Param("date") String date);
}
