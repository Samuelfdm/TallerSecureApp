package edu.eci.arep.TallerSecureApp.controller;

import edu.eci.arep.TallerSecureApp.dto.UserDto;
import edu.eci.arep.TallerSecureApp.model.User;
import edu.eci.arep.TallerSecureApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "https://localhost:8443", allowedHeaders = "*", allowCredentials = "true")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/auth")
    public ResponseEntity<Void> authUser(@RequestBody UserDto userDto) {
        if(userService.auth(userDto)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        User newUser = userService.registerUser(userDto);
        URI uri = URI.create("/user/" + newUser.getId());
        return ResponseEntity.created(uri).body("/users/" + newUser.getId());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
