
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

import com.aegistrack.model.ComponentInspection;
import com.aegistrack.service.ComponentInspectionService;

@RestController
@RequestMapping("/api/components/{componentId}/inspections")
public class ComponentInspectionController {

    private final ComponentInspectionService service;

    public ComponentInspectionController(ComponentInspectionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComponentInspection recordInspection(
            @PathVariable Long componentId,
            @Valid @RequestBody ComponentInspection inspection) {
        return service.recordInspection(componentId, inspection);
    }

    @GetMapping
    public List<ComponentInspection> getInspectionHistory(
            @PathVariable Long componentId) {
        return service.getInspectionHistory(componentId);
    }

    @GetMapping("/search")
    public List<ComponentInspection> getInspectionsByResult(
            @RequestParam String result) {
        return service.getInspectionsByResult(result);
    }
}
