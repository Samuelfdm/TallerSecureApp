package edu.eci.arep.TallerSecureApp.controller;

import edu.eci.arep.TallerSecureApp.model.Property;
import edu.eci.arep.TallerSecureApp.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PropertyController propertyController;

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
        propertyList.add(new Property("Second Address", 200000, 150, "Second Description"));
        
        // Crear una página de properties para las pruebas
        propertyPage = new PageImpl<>(propertyList);
    }

    @Test
    void createProperty_Success() {
        // Configurar mock
        when(propertyService.create(any(Property.class))).thenReturn(testProperty);
        
        // Ejecutar el método
        ResponseEntity<Property> response = propertyController.createProperty(testProperty);
        
        // Verificar
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testProperty, response.getBody());
        verify(propertyService, times(1)).create(testProperty);
    }

    @Test
    void createProperty_Failure() {
        // Configurar mock para devolver null
        when(propertyService.create(any(Property.class))).thenReturn(null);
        
        // Ejecutar el método
        ResponseEntity<Property> response = propertyController.createProperty(testProperty);
        
        // Verificar
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(propertyService, times(1)).create(testProperty);
    }

    @Test
    void getProperties_Success() {
        // Configurar mock
        when(propertyService.getProperties(anyInt(), anyInt())).thenReturn(propertyPage);
        
        // Ejecutar el método
        ResponseEntity<Page<Property>> response = propertyController.getProperties(0, 10);
        
        // Verificar
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(propertyPage, response.getBody());
        verify(propertyService, times(1)).getProperties(0, 10);
    }

    @Test
    void getProperties_EmptyList() {
        // Configurar mock para devolver una página vacía
        when(propertyService.getProperties(anyInt(), anyInt())).thenReturn(Page.empty());
        
        // Ejecutar el método
        ResponseEntity<Page<Property>> response = propertyController.getProperties(0, 10);
        
        // Verificar
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(propertyService, times(1)).getProperties(0, 10);
    }

    @Test
    void getProperty_Success() {
        // Configurar mock
        when(propertyService.getProperty(1L)).thenReturn(testProperty);
        
        // Ejecutar el método
        ResponseEntity<Property> response = propertyController.getProperty(1L);
        
        // Verificar
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testProperty, response.getBody());
        verify(propertyService, times(1)).getProperty(1L);
    }

    @Test
    void getProperty_NotFound() {
        // Configurar mock para devolver null
        when(propertyService.getProperty(999L)).thenReturn(null);
        
        // Ejecutar el método
        ResponseEntity<Property> response = propertyController.getProperty(999L);
        
        // Verificar
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(propertyService, times(1)).getProperty(999L);
    }

    @Test
    void deleteProperty_Success() {
        // Configurar mock para que no lance excepción
        doNothing().when(propertyService).deleteProperty(1L);
        
        // Ejecutar el método
        ResponseEntity<Void> response = propertyController.deleteProperty(1L);
        
        // Verificar
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(propertyService, times(1)).deleteProperty(1L);
    }

    @Test
    void deleteProperty_NotFound() {
        // Configurar mock para lanzar excepción
        doThrow(new IllegalArgumentException("Property not found")).when(propertyService).deleteProperty(999L);
        
        // Ejecutar el método
        ResponseEntity<Void> response = propertyController.deleteProperty(999L);
        
        // Verificar
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(propertyService, times(1)).deleteProperty(999L);
    }

    @Test
    void updateProperty_Success() {
        // Property actualizada
        Property updatedProperty = new Property("Updated Address", 150000, 120, "Updated Description");
        updatedProperty.setId(1L);
        
        // Configurar mock
        when(propertyService.updateProperty(eq(1L), any(Property.class))).thenReturn(updatedProperty);
        
        // Ejecutar el método
        ResponseEntity<Property> response = propertyController.updateProperty(1L, testProperty);
        
        // Verificar
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProperty, response.getBody());
        verify(propertyService, times(1)).updateProperty(eq(1L), any(Property.class));
    }

    @Test
    void updateProperty_NotModified() {
        // Configurar mock para devolver null
        when(propertyService.updateProperty(eq(999L), any(Property.class))).thenReturn(null);
        
        // Ejecutar el método
        ResponseEntity<Property> response = propertyController.updateProperty(999L, testProperty);
        
        // Verificar
        assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
        assertNull(response.getBody());
        verify(propertyService, times(1)).updateProperty(eq(999L), any(Property.class));
    }

    @Test
    void searchProperties_Success() {
        // Configurar mock
        when(propertyService.searchProperties(anyString(), any(PageRequest.class))).thenReturn(propertyPage);
        
        // Ejecutar el método
        ResponseEntity<Page<Property>> response = propertyController.searchProperties("test", 0, 10);
        
        // Verificar
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(propertyPage, response.getBody());
        verify(propertyService, times(1)).searchProperties(eq("test"), any(PageRequest.class));
    }
}