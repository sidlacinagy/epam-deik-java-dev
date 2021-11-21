package com.epam.training.ticketservice.core.pricing.impl;

import com.epam.training.ticketservice.core.pricing.PricingService;
import com.epam.training.ticketservice.core.pricing.persistence.entity.Pricing;
import com.epam.training.ticketservice.core.pricing.persistence.repository.PricingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PricingServiceImpl implements PricingService {

    private final PricingRepository pricingRepository;

    public PricingServiceImpl(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    @Override
    public boolean doesPricingExist(String name) {
        return pricingRepository.findById(name).isPresent();
    }

    @Override
    public String createPricing(String name, int value) {

        if (doesPricingExist(name)) {
            return "Pricing already exists with given name";
        }
        Pricing pricing = new Pricing(name, value);
        pricingRepository.save(pricing);
        return "Successfully created pricing";
    }

    @Override
    public int getValuebyName(String name) {
        Optional<Pricing> byId = pricingRepository.findById(name);
        if (byId.isEmpty()) {
            return 0;
        }
        return byId.get().getValue();
    }
}
