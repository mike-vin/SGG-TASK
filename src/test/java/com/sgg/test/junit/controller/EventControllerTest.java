package com.sgg.test.junit.controller;

import com.sgg.test.controller.EventController;
import com.sgg.test.controller.model.EventRequest;
import com.sgg.test.exceptions.EventNotFoundException;
import com.sgg.test.junit.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.UUID;
import java.util.stream.Stream;

import static com.sgg.test.controller.model.EventType.RED_LIGHT;
import static com.sgg.test.controller.model.EventType.SPEED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class EventControllerTest extends AbstractControllerTest {

    @Autowired
    private EventController eventController;

    @Test
    void shouldSaveSpeedEvent_WhenCreateRequestValid_SUCCESS() {
        EventRequest event = createNewEvent(SPEED);
        event.setSpeed(event.getSpeed());

        eventController.saveEvent(event);

        verify(eventServiceMock).save(event);
        verify(repositoryMock, times(1)).save(event);
    }

    @Test
    void shouldNotSaveSpeedEvent_WhenSpeedLessThanLimit_SUCCESS() {
        EventRequest event = createNewEvent(SPEED);
        event.setSpeed(event.getSpeed() / 2);

        eventController.saveEvent(event);

        verify(eventServiceMock).save(event);
        verify(repositoryMock, never()).save(event);
    }

    @Test
    void shouldSaveRedLightEvent_WhenCreateRequestValid_SUCCESS() {
        EventRequest event = createNewEvent(RED_LIGHT);
        event.setSpeed(event.getSpeed());

        eventController.saveEvent(event);

        verify(eventServiceMock).save(event);
        verify(repositoryMock, times(1)).save(event);
    }

    @Test
    void exceptionExpected_WhenEventNotFoundById_FAIL() {
        assertThrows(EventNotFoundException.class, () -> eventController.findById(UUID.randomUUID()));
    }

    @Test
    void shouldReturnEvent_WhenEventByIdFound_SUCCESS() {
        EventRequest expected = createNewEvent(RED_LIGHT);
        eventController.saveEvent(expected);

        EventRequest actual = eventController.findById(expected.getId());

        assertEquals(expected, actual);
        verify(repositoryMock, times(1)).findById(expected.getId());
    }

    @ParameterizedTest
    @MethodSource("getIllegalFieldsEvents")
    void exceptionExpected_WhenIllegalArgsPresent_FAIL(EventRequest illegalRequest) {
        assertThrows(ConstraintViolationException.class, () -> eventController.saveEvent(illegalRequest));
    }

    public static Stream<EventRequest> getIllegalFieldsEvents() {
        return Stream.of(
                createNewEvent((event) -> event.setId(null)),
                createNewEvent((event) -> event.setEventDate(null)),
                createNewEvent((event) -> event.setEventType(null)),
                createNewEvent((event) -> event.setLicensePlate(null)),
                createNewEvent((event) -> event.setLicensePlate("")),
                createNewEvent((event) -> event.setLicensePlate("ASD-1234")),
                createNewEvent((event) -> event.setLicensePlate("asd-123")),
                createNewEvent((event) -> event.setProcessed(null)));
    }
}