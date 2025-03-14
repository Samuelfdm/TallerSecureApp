package edu.eci.arep.TallerSecureApp.service;

import edu.eci.arep.TallerSecureApp.dto.UserDto;
import edu.eci.arep.TallerSecureApp.model.User;
import edu.eci.arep.TallerSecureApp.repository.jpa.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    JpaUserRepository userRepository;

    @Autowired
    public UserService(JpaUserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public boolean auth(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            return false;
        }
        return BCrypt.checkpw(userDto.getPassword(), user.getPassword());
    }

    public User registerUser(UserDto userDto){
        User newUser = new User(userDto);
        return userRepository.save(newUser);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
