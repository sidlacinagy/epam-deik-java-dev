package com.epam.training.ticketservice.core.pricing.impl;

import com.epam.training.ticketservice.core.pricing.PricingService;
import com.epam.training.ticketservice.core.pricing.persistence.entity.Pricing;
import com.epam.training.ticketservice.core.pricing.persistence.repository.PricingRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PricingServiceImplTest {
    private final PricingRepository pricingRepository = mock(PricingRepository.class);
    private PricingService underTest = new PricingServiceImpl(pricingRepository);

    @Test
    public void testCreatePricingShouldReturnFailWhenPricingAlreadyExists(){
        //Given
        when(pricingRepository.findById("as")).thenReturn(Optional.of(new Pricing("as",5)));

        //When
        String result=underTest.createPricing("as",5);

        //Then
        assertEquals("Pricing already exists with given name",result);

    }

    @Test
    public void testCreatePricingShouldReturnSuccessWhenPricingDoesntExist(){
        //Given
        when(pricingRepository.findById("as")).thenReturn(Optional.empty());

        //When
        String result=underTest.createPricing("as",5);

        //Then
        assertEquals("Successfully created pricing",result);

    }

    @Test
    public void testGetValueByNameShouldReturnValueWhenPricingExists(){
        //Given
        when(pricingRepository.findById("as")).thenReturn(Optional.of(new Pricing("as",5)));

        //When
        int result=underTest.getValuebyName("as");

        //Then
        assertEquals(5,result);

    }

    @Test
    public void testGetValueByNameShouldReturn0WhenPricingDoesntExist(){
        //Given
        when(pricingRepository.findById("as")).thenReturn(Optional.empty());

        //When
        int result=underTest.getValuebyName("as");

        //Then
        assertEquals(0,result);

    }




}
