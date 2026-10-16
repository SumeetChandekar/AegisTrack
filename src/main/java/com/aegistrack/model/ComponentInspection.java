
package com.aegistrack.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "component_inspections")
public class ComponentInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "component_id", nullable = false)
    private EngineeringComponent component;

    @NotNull(message = "Inspection date is required")
    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @NotBlank(message = "Inspector name is required")
    @Size(max = 120, message = "Inspector name must not exceed 120 characters")
    @Column(name = "inspector_name", nullable = false, length = 120)
    private String inspectorName;

    @NotBlank(message = "Inspection result is required")
    @Column(name = "inspection_result", nullable = false, length = 30)
    private String inspectionResult;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    @Column(length = 1000)
    private String notes;

    public ComponentInspection() {
    }

    public Long getId() {
        return id;
    }

    public EngineeringComponent getComponent() {
        return component;
    }

    public void setComponent(EngineeringComponent component) {
        this.component = component;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public String getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
