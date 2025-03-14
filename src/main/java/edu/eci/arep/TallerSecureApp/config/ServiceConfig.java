package edu.eci.arep.TallerSecureApp.config;

import edu.eci.arep.TallerSecureApp.repository.memory.InMemoryPropertyRepository;
import edu.eci.arep.TallerSecureApp.repository.jpa.JpaPropertyRepository;
import edu.eci.arep.TallerSecureApp.repository.PropertyPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ServiceConfig {

    private final String persistenceType;
    private final InMemoryPropertyRepository inMemoryPropertyRepository;
    private final JpaPropertyRepository jpaPropertyRepository;

    @Autowired
    public ServiceConfig(@Value("${property.persistence}") String persistenceType,
                              InMemoryPropertyRepository inMemoryPropertyRepository,
                              JpaPropertyRepository jpaPropertyRepository) {
        this.persistenceType = persistenceType;
        this.inMemoryPropertyRepository = inMemoryPropertyRepository;
        this.jpaPropertyRepository = jpaPropertyRepository;
    }

    @Bean
    @Primary
    public PropertyPersistence servicePersistence() {
        if ("memory".equals(persistenceType)) {
            return inMemoryPropertyRepository;
        } else if ("jpa".equals(persistenceType)) {
            return jpaPropertyRepository;
        } else {
            throw new IllegalArgumentException("Tipo de persistencia no soportado: " + persistenceType);
        }
    }
}
