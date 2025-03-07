package edu.eci.arep.TallerBono.repository.jpa;

import edu.eci.arep.TallerBono.model.Property;
import edu.eci.arep.TallerBono.repository.PropertyPersistence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("jpa")
public interface JpaPropertyRepository extends JpaRepository<Property, Long>, PropertyPersistence {
    @Query("SELECT p FROM Property p ORDER BY p.id DESC")
    Page<Property> findAll(Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.address LIKE %:query% OR p.description LIKE %:query%")
    Page<Property> findByAddressContainingOrDescriptionContaining(@Param("query") String query, Pageable pageable);
}