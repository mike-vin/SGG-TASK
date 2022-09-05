package com.sgg.test.junit.controller;

import com.sgg.test.controller.ViolationsController;
import com.sgg.test.controller.model.EventRequest;
import com.sgg.test.controller.model.ViolationResponse;
import com.sgg.test.controller.model.ViolationSummaryResponse;
import com.sgg.test.junit.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static com.sgg.test.controller.model.EventType.RED_LIGHT;
import static com.sgg.test.controller.model.EventType.SPEED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;

class ViolationsControllerTest extends AbstractControllerTest {

    @Autowired
    private ViolationsController violationsController;

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnEmptyList_WhenNothingFound_SUCCESS() {
        when(eventProperties.getFine()).thenReturn(fine);
        List<ViolationResponse> actual = violationsController.findAll();

        assertTrue(actual.isEmpty());
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnEmptySummary_WhenNothingFound_SUCCESS() {
        when(eventProperties.getFine()).thenReturn(fine);
        ViolationSummaryResponse actual = violationsController.getSummary();

        assertNotNull(actual);
        assertEquals(0, actual.getTotalPaidFines());
        assertEquals(0, actual.getTotalUnpaidFines());
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnSummary_WhenPaidFinesPresentOnly_SUCCESS() {
        when(eventProperties.getFine()).thenReturn(fine);
        EventRequest redLightEvent = createNewEvent(event -> event.setProcessed(true), event -> event.setEventType(RED_LIGHT));
        repositoryMock.save(redLightEvent);

        ViolationSummaryResponse actual = violationsController.getSummary();

        assertNotNull(actual);
        assertEquals(100, actual.getTotalPaidFines());
        assertEquals(0, actual.getTotalUnpaidFines());
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnSummary_WhenUnPaidFinesPresentOnly_SUCCESS() {
        when(eventProperties.getFine()).thenReturn(fine);
        EventRequest redLightEvent = createNewEvent(event -> event.setProcessed(true), event -> event.setEventType(RED_LIGHT));
        repositoryMock.save(redLightEvent);
        repositoryMock.save(createNewEvent(SPEED));

        ViolationSummaryResponse actual = violationsController.getSummary();

        assertNotNull(actual);
        assertEquals(100, actual.getTotalPaidFines());
        assertEquals(50, actual.getTotalUnpaidFines());
    }


    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnSummary_WhenMixedFinesPresent_SUCCESS() {
        when(eventProperties.getFine()).thenReturn(fine);
        repositoryMock.save(createNewEvent(SPEED));

        ViolationSummaryResponse actual = violationsController.getSummary();

        assertNotNull(actual);
        assertEquals(0, actual.getTotalPaidFines());
        assertEquals(50, actual.getTotalUnpaidFines());
    }

}