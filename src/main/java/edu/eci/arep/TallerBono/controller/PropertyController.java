package edu.eci.arep.TallerBono.controller;

import edu.eci.arep.TallerBono.model.Property;
import edu.eci.arep.TallerBono.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService; //Inyectado por constructor

    public PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Property crearProperty(@RequestBody Property property) {
        return propertyService.crear(property);
    }

    @GetMapping("/consult")
    public ResponseEntity<List<Property>> consultarProperties() {
        List<Property> properties = propertyService.obtenerProperties(); // Obtiene la lista de tareas
        if (properties.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Si no hay tareas, devuelve 204 No Content
        }
        return new ResponseEntity<>(properties, HttpStatus.OK); // Devuelve la lista de tareas con 200 OK
    }

    @GetMapping("/consult/{propertyId}")
    public Property consultarProperty(@PathVariable Long tareaId) {
        return propertyService.obtenerProperty(tareaId);
    }

    @DeleteMapping("/delete/{propertyId}")
    public void eliminarProperty(@PathVariable Long tareaId) {
        propertyService.eliminarProperty(tareaId);
    }

    @PutMapping("/update/{propertyId}")
    public void actualizarProperty(@PathVariable Long tareaId, @RequestBody Property nuevaProperty) {
        propertyService.actualizarProperty(tareaId,nuevaProperty);
    }
}
