package edu.eci.arep.TallerBono.repository;

import edu.eci.arep.TallerBono.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PropertyPersistence {
    Page<Property> findAll(Pageable pageable);
    Optional<Property> findById(Long id);
    Property save(Property property);
    void deleteById(Long id);
    Page<Property> findByAddressContainingOrDescriptionContaining(String query, Pageable pageable);
}