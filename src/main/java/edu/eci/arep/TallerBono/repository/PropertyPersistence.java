package edu.eci.arep.TallerBono.repository;

import edu.eci.arep.TallerBono.model.Property;
import java.util.List;
import java.util.Optional;

public interface PropertyPersistence {
    List<Property> findAll();
    Optional<Property> findById(Long id);
    Property save(Property property);
    void deleteById(Long id);
}