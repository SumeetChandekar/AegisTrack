
package com.aegistrack.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aegistrack.model.EngineeringComponent;
import com.aegistrack.repository.EngineeringComponentRepository;

@Service
public class EngineeringComponentService {

    private final EngineeringComponentRepository repository;

    public EngineeringComponentService(EngineeringComponentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public EngineeringComponent registerComponent(EngineeringComponent component) {
        if (repository.existsBySerialNumber(component.getSerialNumber())) {
            throw new IllegalArgumentException(
                    "A component with this serial number already exists.");
        }

        if (component.getLifecycleStatus() == null
                || component.getLifecycleStatus().isBlank()) {
            component.setLifecycleStatus("REGISTERED");
        }

        component.setLifecycleStatus(
                component.getLifecycleStatus().trim().toUpperCase());

        return repository.save(component);
    }

    @Transactional(readOnly = true)
    public List<EngineeringComponent> getAllComponents() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public EngineeringComponent getComponentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Engineering component not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<EngineeringComponent> getComponentsByStatus(String status) {
        return repository.findByLifecycleStatus(status.trim().toUpperCase());
    }
}
