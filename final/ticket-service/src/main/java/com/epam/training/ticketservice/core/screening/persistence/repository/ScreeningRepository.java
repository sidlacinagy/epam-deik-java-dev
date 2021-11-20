package com.epam.training.ticketservice.core.screening.persistence.repository;

import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Screening.Key> {

    @Query("SELECT r FROM Screening r WHERE r.key.roomName = :room")
    List<Screening> findByRoom(@Param("room") String room);

}
