package edu.eci.arep.TallerBono.service;

import edu.eci.arep.TallerBono.model.Property;
import edu.eci.arep.TallerBono.repository.PropertyPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyPersistence propertyPersistence;

    @Autowired
    public PropertyService(PropertyPersistence propertyPersistence) {
        this.propertyPersistence = propertyPersistence;
    }

    public Property crear(Property property) {
        return propertyPersistence.save(property);
    }

    public List<Property> obtenerProperties() {
        return propertyPersistence.findAll();
    }

    public Property obtenerProperty(Long propertyId) {
        Optional<Property> property = propertyPersistence.findById(propertyId);
        return property.orElseThrow(() -> new IllegalArgumentException("Property con ID " + propertyId + " no existe"));
    }

    public void eliminarProperty(Long propertyId) {
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

    public void actualizarProperty(Long propertyId, Property nuevoProperty) {
        Property property = obtenerProperty(propertyId);
        property.setAddress(nuevoProperty.getAddress());
        property.setPrice(nuevoProperty.getAddress());
        property.setSize(nuevoProperty.getAddress());
        property.setDescription(nuevoProperty.getDescription());
        propertyPersistence.save(property);
    }
}