package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
public class UserCommand {

    private final UserService userService;

    public UserCommand(UserService userService) {
        this.userService = userService;
    }

    @ShellMethod(key = "sign in", value = "User login")
    public String loginUser(String username, String password) {
        Optional<UserDto> user = userService.login(username, password);
        if (user.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        if(user.get().getRole()==User.Role.USER) {
            return username + " is logged in!";
        }
        user = userService.logout();
        return "Login failed due to incorrect credentials";

    }

    @ShellMethod(key = "sign in privileged", value = "User login")
    public String loginAdmin(String username, String password) {
        Optional<UserDto> user = userService.login(username, password);
        if (user.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        if(user.get().getRole()==User.Role.ADMIN) {
            return username + " is logged in!";
        }
        user = userService.logout();
        return "Login failed due to incorrect credentials";
    }

    @ShellMethod(key = "sign out", value = "User logout")
    public String logout() {
        Optional<UserDto> user = userService.logout();
        if (user.isEmpty()) {
            return "You need to login first!";
        }
        return user.get() + " is logged out!";
    }

    @ShellMethod(key = "describe account", value = "Get user information")
    public String printLoggedInUser() {
        Optional<UserDto> userDto = userService.getLoggedInUser();
        if (userDto.isEmpty()) {
            return "You need to login first!";
        }
        if (userDto.get().getRole() == User.Role.ADMIN){
            return "Signed in with privileged account: "+userDto.get().getUsername();
        }
        else if (userDto.get().getRole() == User.Role.USER){
            return "Signed in with account: "+userDto.get().getUsername();
        }
        return userDto.get().toString();
    }

    @ShellMethod(key = "sign up", value = "User registration")
    public String registerUser(String userName, String password) {
        try {
            userService.registerUser(userName, password);
            return "Registration was successful!";
        } catch (Exception e) {
            return "Registration failed!";
        }
    }
}
