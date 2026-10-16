
package com.aegistrack.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.aegistrack.model.EngineeringComponent;
import com.aegistrack.service.EngineeringComponentService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EngineeringComponentController.class)
class EngineeringComponentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EngineeringComponentService service;

    @Test
    void getAllComponentsReturnsOk() throws Exception {
        EngineeringComponent component = createComponent(1L, "UNDER_REVIEW");

        when(service.getAllComponents()).thenReturn(List.of(component));

        mockMvc.perform(get("/api/components"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Sensor"));

        verify(service).getAllComponents();
    }

    @Test
    void getComponentsByStatusReturnsFilteredResults() throws Exception {
        EngineeringComponent component = createComponent(1L, "UNDER_REVIEW");

        when(service.getComponentsByStatus("UNDER_REVIEW"))
                .thenReturn(List.of(component));

        mockMvc.perform(get("/api/components")
                        .param("status", "UNDER_REVIEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lifecycleStatus")
                        .value("UNDER_REVIEW"));

        verify(service).getComponentsByStatus("UNDER_REVIEW");
    }

    @Test
    void getComponentByIdReturnsComponent() throws Exception {
        EngineeringComponent component = createComponent(1L, "REGISTERED");

        when(service.getComponentById(1L)).thenReturn(component);

        mockMvc.perform(get("/api/components/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.serialNumber").value("AT-TEST-001"));

        verify(service).getComponentById(1L);
    }

    @Test
    void missingComponentReturnsNotFound() throws Exception {
        when(service.getComponentById(999999L))
                .thenThrow(new IllegalArgumentException(
                        "Engineering component not found with ID: 999999"));

        mockMvc.perform(get("/api/components/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Engineering component not found with ID: 999999"));
    }

    @Test
    void registerComponentReturnsCreated() throws Exception {
        EngineeringComponent saved = createComponent(2L, "REGISTERED");

        when(service.registerComponent(any(EngineeringComponent.class)))
                .thenReturn(saved);

        String requestBody = """
                {
                    "name": "Test Sensor",
                    "serialNumber": "AT-TEST-001",
                    "manufacturer": "Test Instruments Ltd",
                    "componentType": "SENSOR",
                    "lifecycleStatus": "REGISTERED",
                    "receivedDate": "2026-10-16",
                    "notes": "Controller test"
                }
                """;

        mockMvc.perform(post("/api/components")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Test Sensor"));

        verify(service).registerComponent(any(EngineeringComponent.class));
    }

    @Test
    void registerComponentWithBlankNameReturnsBadRequest() throws Exception {
        String requestBody = """
                {
                    "name": "",
                    "serialNumber": "AT-TEST-INVALID",
                    "lifecycleStatus": "REGISTERED"
                }
                """;

        mockMvc.perform(post("/api/components")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    private EngineeringComponent createComponent(Long id, String lifecycleStatus) {
        EngineeringComponent component = new EngineeringComponent();

        // The ID is generated by the database in production.
        // This helper uses reflection-free test setup for response assertions.
        try {
            var idField = EngineeringComponent.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(component, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }

        component.setName("Test Sensor");
        component.setSerialNumber("AT-TEST-001");
        component.setManufacturer("Test Instruments Ltd");
        component.setComponentType("SENSOR");
        component.setLifecycleStatus(lifecycleStatus);
        component.setReceivedDate(LocalDate.of(2026, 10, 16));
        component.setNotes("Controller test");

        return component;
    }
}
