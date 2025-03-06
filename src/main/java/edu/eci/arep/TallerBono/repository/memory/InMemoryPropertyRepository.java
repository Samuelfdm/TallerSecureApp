package edu.eci.arep.TallerBono.repository.memory;

import edu.eci.arep.TallerBono.model.Property;
import edu.eci.arep.TallerBono.repository.PropertyPersistence;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("memory")
public class InMemoryPropertyRepository implements PropertyPersistence {
    private final List<Property> properties = new ArrayList<>();
    private Long currentId = 1L;

    @Override
    public List<Property> findAll() {
        return new ArrayList<>(properties);
    }

    @Override
    public Optional<Property> findById(Long id) {
        return properties.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @Override
    public Property save(Property property) {
        if (property.getId() == null) {
            property.setId(currentId++);
        }
        properties.removeIf(p -> p.getId().equals(property.getId()));
        properties.add(property);
        return property;
    }

    @Override
    public void deleteById(Long id) {
        properties.removeIf(p -> p.getId().equals(id));
    }
}