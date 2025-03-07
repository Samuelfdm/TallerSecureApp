package edu.eci.arep.TallerBono.controller;

import edu.eci.arep.TallerBono.model.Property;
import edu.eci.arep.TallerBono.service.PropertyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService; //Inyectado por constructor

    public PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }

    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        Property propertyCreated = propertyService.create(property);
        if (propertyCreated == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Si no creó la property, devuelve 204 No Content
        }
        return new ResponseEntity<>(propertyCreated, HttpStatus.CREATED); //Devuelve la property con 200 OK
    }

    @GetMapping
    public ResponseEntity<Page<Property>> getProperties(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "3") int size) {
        Page<Property> properties = propertyService.getProperties(page, size); // Obtiene la lista de properties
        if (properties.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no hay properties, devuelve 404 Not Found
        }
        return new ResponseEntity<>(properties, HttpStatus.OK); // Devuelve la lista de properties con 200 OK
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<Property> getProperty(@PathVariable Long propertyId) {
        Property property = propertyService.getProperty(propertyId);
        if (property == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no está la property, devuelve 404 Not Found
        }
        return new ResponseEntity<>(property, HttpStatus.OK); //Devuelve la property con 200 OK
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long propertyId) {
        try {
            propertyService.deleteProperty(propertyId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{propertyId}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long propertyId, @RequestBody Property nuevaProperty) {
        Property property = propertyService.updateProperty(propertyId,nuevaProperty);
        if (property == null) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED); // Si no actualizó la property, devuelve 204 No Content
        }
        return new ResponseEntity<>(property, HttpStatus.OK); //Devuelve la property actualizada con 200 OK
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Property>> searchProperties(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Page<Property> properties = propertyService.searchProperties(query, PageRequest.of(page, size));
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }
}