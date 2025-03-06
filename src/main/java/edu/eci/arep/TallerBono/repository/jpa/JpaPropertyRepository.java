package edu.eci.arep.TallerBono.repository.jpa;

import edu.eci.arep.TallerBono.model.Property;
import edu.eci.arep.TallerBono.repository.PropertyPersistence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("jpa")
public interface JpaPropertyRepository extends JpaRepository<Property, Long>, PropertyPersistence {
}