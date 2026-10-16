package com.aegistrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import com.aegistrack.model.ComponentInspection;
import com.aegistrack.model.EngineeringComponent;
import com.aegistrack.repository.ComponentInspectionRepository;
import com.aegistrack.repository.EngineeringComponentRepository;
import com.aegistrack.service.ComponentInspectionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ComponentInspectionServiceTests {

    @Mock
    private ComponentInspectionRepository inspectionRepository;

    @Mock
    private EngineeringComponentRepository componentRepository;

    private ComponentInspectionService service;
    private EngineeringComponent component;

    @BeforeEach
    void setUp() {
        service = new ComponentInspectionService(
                inspectionRepository, componentRepository);

        component = new EngineeringComponent();
    }

    @Test
    void failedInspectionSetsComponentUnderReview() {
        when(componentRepository.findById(1L))
                .thenReturn(Optional.of(component));

        when(inspectionRepository.save(any(ComponentInspection.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ComponentInspection inspection = new ComponentInspection();
        inspection.setInspectionDate(LocalDate.of(2026, 10, 16));
        inspection.setInspectorName("Unit Test Inspector");
        inspection.setInspectionResult("FAIL");

        service.recordInspection(1L, inspection);

        assertEquals("UNDER_REVIEW", component.getLifecycleStatus());
        assertEquals(LocalDate.of(2026, 10, 16),
                component.getLastInspectionDate());
        assertEquals("FAIL", inspection.getInspectionResult());

        verify(componentRepository).save(component);
    }

    @Test
    void invalidInspectionResultIsRejected() {
        when(componentRepository.findById(1L))
                .thenReturn(Optional.of(component));

        ComponentInspection inspection = new ComponentInspection();
        inspection.setInspectionDate(LocalDate.of(2026, 10, 16));
        inspection.setInspectorName("Unit Test Inspector");
        inspection.setInspectionResult("INVALID");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.recordInspection(1L, inspection));

        assertEquals(
                "Inspection result must be PASS, FAIL, or CONDITIONAL.",
                exception.getMessage());

        verify(inspectionRepository, never())
                .save(any(ComponentInspection.class));
        verify(componentRepository, never())
                .save(any(EngineeringComponent.class));
    }

    @Test
    void passedInspectionUpdatesDateWithoutClearingReviewStatus() {
        component.setLifecycleStatus("UNDER_REVIEW");

        when(componentRepository.findById(1L))
                .thenReturn(Optional.of(component));

        when(inspectionRepository.save(any(ComponentInspection.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ComponentInspection inspection = new ComponentInspection();
        inspection.setInspectionDate(LocalDate.of(2026, 10, 16));
        inspection.setInspectorName("Unit Test Inspector");
        inspection.setInspectionResult(" pass ");

        ComponentInspection savedInspection =
                service.recordInspection(1L, inspection);

        assertEquals("PASS", savedInspection.getInspectionResult());
        assertEquals(
                LocalDate.of(2026, 10, 16),
                component.getLastInspectionDate());
        assertEquals("UNDER_REVIEW", component.getLifecycleStatus());

        verify(inspectionRepository).save(inspection);
        verify(componentRepository).save(component);
    }
}