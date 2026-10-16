
package com.aegistrack.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aegistrack.model.EngineeringComponent;
import com.aegistrack.service.EngineeringComponentService;

@RestController
@RequestMapping("/api/components")
public class EngineeringComponentController {

    private final EngineeringComponentService service;

    public EngineeringComponentController(EngineeringComponentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EngineeringComponent registerComponent(
            @Valid @RequestBody EngineeringComponent component) {
        return service.registerComponent(component);
    }

    @GetMapping
    public List<EngineeringComponent> getComponents(
            @RequestParam(required = false) String status) {
        if (status != null && !status.isBlank()) {
            return service.getComponentsByStatus(status);
        }
        return service.getAllComponents();
    }

    @GetMapping("/{id}")
    public EngineeringComponent getComponentById(@PathVariable Long id) {
        return service.getComponentById(id);
    }
}
