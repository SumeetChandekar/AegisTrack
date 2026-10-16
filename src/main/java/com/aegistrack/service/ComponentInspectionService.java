
package com.aegistrack.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aegistrack.model.ComponentInspection;
import com.aegistrack.model.EngineeringComponent;
import com.aegistrack.repository.ComponentInspectionRepository;
import com.aegistrack.repository.EngineeringComponentRepository;

@Service
public class ComponentInspectionService {

    private final ComponentInspectionRepository inspectionRepository;
    private final EngineeringComponentRepository componentRepository;

    public ComponentInspectionService(
            ComponentInspectionRepository inspectionRepository,
            EngineeringComponentRepository componentRepository) {
        this.inspectionRepository = inspectionRepository;
        this.componentRepository = componentRepository;
    }

    @Transactional
    public ComponentInspection recordInspection(
            Long componentId, ComponentInspection inspection) {

        EngineeringComponent component = componentRepository.findById(componentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Engineering component not found with ID: " + componentId));

        String result = inspection.getInspectionResult().trim().toUpperCase();

        if (!result.equals("PASS")
                && !result.equals("FAIL")
                && !result.equals("CONDITIONAL")) {
            throw new IllegalArgumentException(
                    "Inspection result must be PASS, FAIL, or CONDITIONAL.");
        }

        inspection.setInspectionResult(result);
        inspection.setComponent(component);

        ComponentInspection savedInspection =
                inspectionRepository.save(inspection);

        component.setLastInspectionDate(inspection.getInspectionDate());

        if (result.equals("FAIL")) {
            component.setLifecycleStatus("UNDER_REVIEW");
        }

        componentRepository.save(component);

        return savedInspection;
    }

    @Transactional(readOnly = true)
    public List<ComponentInspection> getInspectionHistory(Long componentId) {
        if (!componentRepository.existsById(componentId)) {
            throw new IllegalArgumentException(
                    "Engineering component not found with ID: " + componentId);
        }

        return inspectionRepository
                .findByComponent_IdOrderByInspectionDateDesc(componentId);
    }

    @Transactional(readOnly = true)
    public List<ComponentInspection> getInspectionsByResult(String result) {
        return inspectionRepository
                .findByInspectionResultIgnoreCase(result.trim());
    }
}
