package edu.eci.arep.TallerSecureApp.model;

import edu.eci.arep.TallerSecureApp.dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;

    public User() {
    }

    public User(String email, String password) {
        this.id = null;
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public User(UserDto userDto){
        this.id = null;
        this.email = userDto.getEmail();
        this.password = new BCryptPasswordEncoder().encode(userDto.getPassword());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
