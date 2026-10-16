
package com.aegistrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aegistrack.model.ComponentInspection;

public interface ComponentInspectionRepository
        extends JpaRepository<ComponentInspection, Long> {

    List<ComponentInspection> findByComponent_IdOrderByInspectionDateDesc(
            Long componentId);

    List<ComponentInspection> findByInspectionResultIgnoreCase(
            String inspectionResult);
}
