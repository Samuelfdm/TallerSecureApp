package edu.eci.arep.TallerBono.repository;

import edu.eci.arep.TallerBono.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PropertyPersistence extends JpaRepository<Property, Long> {
//    Property save(Property tarea);
//    Optional<Property> findById(Long id);
//    List<Property> findAll();
//    void deleteById(Long id);
}