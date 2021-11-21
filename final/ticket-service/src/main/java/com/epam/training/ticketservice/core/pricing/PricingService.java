package com.epam.training.ticketservice.core.pricing;

import com.epam.training.ticketservice.core.room.model.RoomDto;

public interface PricingService {
    boolean doesPricingExist(String name);

    String createPricing(String name,int value);

    int getValuebyName(String name);

}
