package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;

import java.util.List;

public interface ScreeningService {

    String createScreening(ScreeningDto screeningDto);

    void deleteScreening(ScreeningDto screeningDto);

    List<ScreeningDto> getScreeningList();

    List<Screening> findAll();


}
