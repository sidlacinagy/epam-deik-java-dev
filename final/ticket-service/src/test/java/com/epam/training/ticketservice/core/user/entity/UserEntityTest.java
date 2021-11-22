package com.epam.training.ticketservice.core.user.entity;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserEntityTest {

    @Test
    public void testEqualsShouldReturnTrue(){
        //Given
        User underTest=new User("admin","admin", User.Role.ADMIN);

        //When
        User expected=new User("admin","admin", User.Role.ADMIN);

        //Then
        assertEquals(expected, underTest);
    }

    @Test
    public void testEqualsShouldReturnFalse(){
        //Given
        User underTest=new User("admin","admin", User.Role.ADMIN);

        //When
        User expected=new User("admi1n","admin", User.Role.ADMIN);

        //Then
        assertNotEquals(expected, underTest);
    }

}
