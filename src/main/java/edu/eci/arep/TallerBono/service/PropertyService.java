package edu.eci.arep.TallerBono.service;

import edu.eci.arep.TallerBono.model.Property;
import edu.eci.arep.TallerBono.repository.PropertyPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyPersistence propertyPersistence;

    @Autowired
    public PropertyService(PropertyPersistence propertyPersistence) {
        this.propertyPersistence = propertyPersistence;
    }

    public Property create(Property property) {
        return propertyPersistence.save(property);
    }

    public Page<Property> getProperties(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return propertyPersistence.findAll(pageable);
    }

    public Property getProperty(Long propertyId) {
        Optional<Property> property = propertyPersistence.findById(propertyId);
        return property.orElseThrow(() -> new IllegalArgumentException("Property con ID " + propertyId + " no existe"));
    }

    @Transactional
    public void deleteProperty(Long propertyId) {
        // Verifica si el ID del property es nulo o vacío
        if (propertyId == null) {
            throw new IllegalArgumentException("El ID de la tarea no puede ser nulo o vacío");
        }

        // Verifica si el property existe antes de intentar eliminarlo
        Optional<Property> property = propertyPersistence.findById(propertyId);
        if (property.isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar la tarea. La tarea con ID " + propertyId + " no existe.");
        }

        // Procedemos a eliminarlo
        propertyPersistence.deleteById(propertyId);

    }

    public Property updateProperty(Long propertyId, Property nuevoProperty) {
        Property property = getProperty(propertyId);
        property.setAddress(nuevoProperty.getAddress());
        property.setPrice(nuevoProperty.getPrice());
        property.setSize(nuevoProperty.getSize());
        property.setDescription(nuevoProperty.getDescription());
        return propertyPersistence.save(property);
    }

    public Page<Property> searchProperties(String query, Pageable pageable) {
        return propertyPersistence.findByAddressContainingOrDescriptionContaining(query, pageable);
    }
}