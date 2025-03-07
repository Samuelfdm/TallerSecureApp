package edu.eci.arep.TallerBono.repository.memory;

import edu.eci.arep.TallerBono.model.Property;
import edu.eci.arep.TallerBono.repository.PropertyPersistence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("memory")
public class InMemoryPropertyRepository implements PropertyPersistence {
    private final List<Property> properties = new ArrayList<>();
    private Long currentId = 1L;

    @Override
    public Page<Property> findAll(Pageable pageable) {
        // Ordenar propiedades por ID descendente
        List<Property> sortedProperties = properties.stream()
                .sorted(Comparator.comparing(Property::getId).reversed())
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sortedProperties.size());
        List<Property> pageContent = sortedProperties.subList(start, end);

        return new PageImpl<>(pageContent, pageable, sortedProperties.size());
    }

    @Override
    public Optional<Property> findById(Long id) {
        return properties.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
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

    @Override
    public Page<Property> findByAddressContainingOrDescriptionContaining(String query, Pageable pageable) {
        // Filtrar propiedades por dirección o descripción y ordenar por ID descendente
        List<Property> filteredProperties = properties.stream()
                .filter(p -> p.getAddress().contains(query) || p.getDescription().contains(query))
                .sorted(Comparator.comparing(Property::getId).reversed())
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredProperties.size());
        List<Property> pageContent = filteredProperties.subList(start, end);

        return new PageImpl<>(pageContent, pageable, filteredProperties.size());
    }
}