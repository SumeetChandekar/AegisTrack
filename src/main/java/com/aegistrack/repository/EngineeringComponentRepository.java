
package com.aegistrack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aegistrack.model.EngineeringComponent;

public interface EngineeringComponentRepository
        extends JpaRepository<EngineeringComponent, Long> {

    Optional<EngineeringComponent> findBySerialNumber(String serialNumber);

    boolean existsBySerialNumber(String serialNumber);

    List<EngineeringComponent> findByLifecycleStatus(String lifecycleStatus);
}
