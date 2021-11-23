package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.impl.BookingServiceImpl;
import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserCommandTest {

    private final UserServiceImpl userService=mock(UserServiceImpl.class);
    private final BookingServiceImpl bookingService=mock(BookingServiceImpl.class);
    private final ScreeningServiceImpl screeningService=mock(ScreeningServiceImpl.class);

    private final UserCommand underTest= new UserCommand(userService,bookingService,screeningService);

    @Test
    public void testLoginUserShouldReturnFailWhenCredentialsAreIncorrect(){
        //Given
        when(userService.login("adminkk","adminkj")).thenReturn(Optional.empty());
        String expected="Login failed due to incorrect credentials";

        //When
        String result=underTest.loginUser("adminkk","adminkj");

        //Then
        assertEquals(expected,result);
    }
    @Test
    public void testLoginUserShouldReturnSuccessWhenCredentialsAreCorrect(){
        //Given
        when(userService.login("adminkk","adminkj")).thenReturn(Optional.of(new UserDto("adminkk", User.Role.USER)));
        String expected="adminkk is logged in!";

        //When
        String result=underTest.loginUser("adminkk","adminkj");

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testLoginUserShouldReturnFailWhenLoggedInUserIsAdmin(){
        //Given
        when(userService.login("adminkk","adminkj")).thenReturn(Optional.of(new UserDto("adminkk", User.Role.ADMIN)));
        String expected="Login failed due to incorrect credentials";

        //When
        String result=underTest.loginUser("adminkk","adminkj");

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testLoginAdminShouldReturnFailWhenCredentialsAreIncorrect(){
        //Given
        when(userService.login("adminkk","adminkj")).thenReturn(Optional.empty());
        String expected="Login failed due to incorrect credentials";

        //When
        String result=underTest.loginAdmin("adminkk","adminkj");

        //Then
        assertEquals(expected,result);
    }
    @Test
    public void testLoginAdminShouldReturnSuccessWhenCredentialsAreCorrect(){
        //Given
        when(userService.login("adminkk","adminkj")).thenReturn(Optional.of(new UserDto("adminkk", User.Role.ADMIN)));
        String expected="adminkk is logged in!";

        //When
        String result=underTest.loginAdmin("adminkk","adminkj");

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testLoginAdminShouldReturnFailWhenLoggedInUserIsUser(){
        //Given
        when(userService.login("adminkk","adminkj")).thenReturn(Optional.of(new UserDto("adminkk", User.Role.USER)));
        String expected="Login failed due to incorrect credentials";

        //When
        String result=underTest.loginAdmin("adminkk","adminkj");

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testLogoutShouldReturnSuccessWhenUserIsLoggedIn(){
        //Given
        when(userService.logout()).thenReturn(Optional.of(new UserDto("adminkk", User.Role.USER)));
        String expected="adminkk is logged out!";

        //When
        String result=underTest.logout();

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testLogoutShouldReturnFailWhenUserIsNotLoggedIn(){
        //Given
        when(userService.logout()).thenReturn(Optional.empty());
        String expected="You are not signed in";

        //When
        String result=underTest.logout();

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testShowPriceForShouldReturnPriceWhenScreeningExists(){
        //Given
        when(screeningService.findById(new Screening.Key("dummyMovie","dummyRoom","2000")))
                .thenReturn(Optional.of(new Screening("dummyMovie","dummyRoom","2000","price")));
        when(bookingService.getPriceForBooking(new Screening("dummyMovie","dummyRoom","2000","price")))
                .thenReturn(1200);
        String expected="The price for this booking would be 3600 HUF";

        //When
        String result=underTest.show("dummyMovie","dummyRoom","2000","5,5 6,6 7,7");

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testShowPriceForShouldReturnFailWhenScreeningDoesntExist(){
        //Given
        when(screeningService.findById(new Screening.Key("dummyMovie","dummyRoom","2000")))
                .thenReturn(Optional.empty());
        String expected="No such screening";

        //When
        String result=underTest.show("dummyMovie","dummyRoom","2000","5,5 6,6 7,7");

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testPrintLoggedInUserShouldReturnFailWhenUserIsNotLoggedIn(){
        //Given
        when(userService.getLoggedInUser()).thenReturn(Optional.empty());
        String expected="You are not signed in";

        //When
        String result=underTest.printLoggedInUser();

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testPrintLoggedInUserShouldReturnLoggedInAdminWhenAdminIsLoggedIn(){
        //Given
        when(userService.getLoggedInUser()).thenReturn(Optional.of(new UserDto("admin", User.Role.ADMIN)));
        String expected="Signed in with privileged account 'admin'";

        //When
        String result=underTest.printLoggedInUser();

        //Then
        assertEquals(expected,result);
    }

    @Test
    public void testPrintLoggedInUserShouldReturnLoggedInUserAndDetailsWhenUserIsLoggedIn(){
        //Given
        when(userService.getLoggedInUser()).thenReturn(Optional.of(new UserDto("jos", User.Role.USER)));
        when(bookingService.listBookingsForUser("jos")).thenReturn("bookings");
        String expected="Signed in with account 'jos'\nbookings";

        //When
        String result=underTest.printLoggedInUser();

        //Then
        assertEquals(expected,result);
    }




}
