package com.hakaton.challenge.api;

import com.hakaton.challenge.service.UserService;
import com.hakaton.challenge.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user)
    {
        if(!user.isValid())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email!");
        user = userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


}
