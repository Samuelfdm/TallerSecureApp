package edu.eci.arep.TallerSecureApp.repository.jpa;

import edu.eci.arep.TallerSecureApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}