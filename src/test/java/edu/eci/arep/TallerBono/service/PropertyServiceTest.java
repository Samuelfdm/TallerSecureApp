package edu.eci.arep.TallerBono.service;

import edu.eci.arep.TallerBono.model.Property;
import edu.eci.arep.TallerBono.repository.PropertyPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PropertyServiceTest {

    @Mock
    private PropertyPersistence propertyPersistence;

    @InjectMocks
    private PropertyService propertyService;

    private Property testProperty;
    private List<Property> propertyList;
    private Page<Property> propertyPage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Crear un property de prueba
        testProperty = new Property("Test Address", 100000, 100, "Test Description");
        testProperty.setId(1L);
        
        // Crear una lista de properties para las pruebas
        propertyList = new ArrayList<>();
        propertyList.add(testProperty);
        
        // Crear una página de properties para las pruebas
        propertyPage = new PageImpl<>(propertyList);
    }

    @Test
    void create_Success() {
        // Configurar mock
        when(propertyPersistence.save(any(Property.class))).thenReturn(testProperty);
        
        // Ejecutar el método
        Property result = propertyService.create(testProperty);
        
        // Verificar
        assertNotNull(result);
        assertEquals(testProperty, result);
        verify(propertyPersistence, times(1)).save(testProperty);
    }

    @Test
    void getProperties_Success() {
        // Configurar mock
        when(propertyPersistence.findAll(any(Pageable.class))).thenReturn(propertyPage);
        
        // Ejecutar el método
        Page<Property> result = propertyService.getProperties(0, 10);
        
        // Verificar
        assertNotNull(result);
        assertEquals(propertyPage, result);
        verify(propertyPersistence, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getProperty_Success() {
        // Configurar mock
        when(propertyPersistence.findById(1L)).thenReturn(Optional.of(testProperty));
        
        // Ejecutar el método
        Property result = propertyService.getProperty(1L);
        
        // Verificar
        assertNotNull(result);
        assertEquals(testProperty, result);
        verify(propertyPersistence, times(1)).findById(1L);
    }

    @Test
    void getProperty_NotFound() {
        // Configurar mock para devolver Optional vacío
        when(propertyPersistence.findById(999L)).thenReturn(Optional.empty());
        
        // Ejecutar el método y verificar que lance excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            propertyService.getProperty(999L);
        });
        
        // Verificar el mensaje de la excepción
        assertTrue(exception.getMessage().contains("Property con ID 999 no existe"));
        verify(propertyPersistence, times(1)).findById(999L);
    }

    @Test
    void deleteProperty_Success() {
        // Configurar mock
        when(propertyPersistence.findById(1L)).thenReturn(Optional.of(testProperty));
        doNothing().when(propertyPersistence).deleteById(1L);
        
        // Ejecutar el método
        assertDoesNotThrow(() -> propertyService.deleteProperty(1L));
        
        // Verificar
        verify(propertyPersistence, times(1)).findById(1L);
        verify(propertyPersistence, times(1)).deleteById(1L);
    }

    @Test
    void deleteProperty_NullId() {
        // Ejecutar el método y verificar que lance excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            propertyService.deleteProperty(null);
        });
        
        // Verificar el mensaje de la excepción
        assertTrue(exception.getMessage().contains("El ID de la tarea no puede ser nulo o vacío"));
        verify(propertyPersistence, never()).deleteById(any());
    }

    @Test
    void deleteProperty_NotFound() {
        // Configurar mock para devolver Optional vacío
        when(propertyPersistence.findById(999L)).thenReturn(Optional.empty());
        
        // Ejecutar el método y verificar que lance excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            propertyService.deleteProperty(999L);
        });
        
        // Verificar el mensaje de la excepción
        assertTrue(exception.getMessage().contains("No se puede eliminar la tarea. La tarea con ID 999 no existe."));
        verify(propertyPersistence, times(1)).findById(999L);
        verify(propertyPersistence, never()).deleteById(any());
    }

    @Test
    void updateProperty_Success() {
        // Property actualizada
        Property updatedProperty = new Property("Updated Address", 150000, 120, "Updated Description");
        
        // Configurar mocks
        when(propertyPersistence.findById(1L)).thenReturn(Optional.of(testProperty));
        when(propertyPersistence.save(any(Property.class))).thenAnswer(i -> i.getArgument(0));
        
        // Ejecutar el método
        Property result = propertyService.updateProperty(1L, updatedProperty);
        
        // Verificar
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(updatedProperty.getAddress(), result.getAddress());
        assertEquals(updatedProperty.getPrice(), result.getPrice());
        assertEquals(updatedProperty.getSize(), result.getSize());
        assertEquals(updatedProperty.getDescription(), result.getDescription());
        verify(propertyPersistence, times(1)).findById(1L);
        verify(propertyPersistence, times(1)).save(any(Property.class));
    }

    @Test
    void searchProperties_Success() {
        // Configurar mock
        when(propertyPersistence.findByAddressContainingOrDescriptionContaining(anyString(), any(Pageable.class)))
            .thenReturn(propertyPage);
        
        // Ejecutar el método
        Page<Property> result = propertyService.searchProperties("test", PageRequest.of(0, 10));
        
        // Verificar
        assertNotNull(result);
        assertEquals(propertyPage, result);
        verify(propertyPersistence, times(1))
            .findByAddressContainingOrDescriptionContaining(eq("test"), any(Pageable.class));
    }
}