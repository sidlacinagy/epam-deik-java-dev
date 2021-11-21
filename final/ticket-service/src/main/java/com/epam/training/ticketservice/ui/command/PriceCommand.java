package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.pricing.PricingService;
import com.epam.training.ticketservice.core.pricing.persistence.entity.Pricing;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class PriceCommand {
    private final UserService userService;
    private final BookingService bookingService;
    private final PricingService pricingService;

    public PriceCommand(UserService userService, BookingService bookingService, PricingService pricingService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.pricingService = pricingService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update base price", value = "update base price")
    public String updatePrice(int price) {
        bookingService.changePrice(price);
        return "Successfully updated";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create price component", value = "create price component")
    public String createPrice(String priceName,int value) {
        pricingService.createPricing(priceName,value);
        return "Successfully updated";
    }


    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }
}
