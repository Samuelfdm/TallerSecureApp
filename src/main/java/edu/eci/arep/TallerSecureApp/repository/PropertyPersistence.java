package edu.eci.arep.TallerSecureApp.repository;

import edu.eci.arep.TallerSecureApp.model.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PropertyPersistence {
    Page<Property> findAll(Pageable pageable);
    Optional<Property> findById(Long id);
    Property save(Property property);
    void deleteById(Long id);
    Page<Property> findByAddressContainingOrDescriptionContaining(String query, Pageable pageable);
}