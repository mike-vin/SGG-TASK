package com.sgg.test.junit;

import com.sgg.test.Application;
import com.sgg.test.config.EventProperties;
import com.sgg.test.controller.model.EventRequest;
import com.sgg.test.controller.model.EventType;
import com.sgg.test.dao.EventRepository;
import com.sgg.test.service.EventService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Application.class)
public abstract class AbstractControllerTest {

    protected static EventProperties.Fine fine;

    @SpyBean
    protected EventRepository repositoryMock;
    @SpyBean
    protected EventService eventServiceMock;
    @SpyBean
    protected EventProperties eventProperties;

    @BeforeAll
    static void  setUp() {
        fine = new EventProperties.Fine();
        fine.setRedLightRate(100);
        fine.setSpeedRate(50);
    }

    protected static EventRequest createNewEvent(EventType eventType) {
        EventRequest request = new EventRequest();
        request.setId(UUID.randomUUID());
        request.setEventType(eventType);
        request.setEventDate(LocalDateTime.now());
        request.setSpeed(100);
        request.setLimit(50);
        request.setUnity("km/h");
        request.setLicensePlate("ABC-123");
        request.setProcessed(false);
        return request;
    }

    @SafeVarargs
    protected static EventRequest createNewEvent(Consumer<EventRequest>... consumers) {
        EventRequest request = createNewEvent(EventType.SPEED);
        for (Consumer<EventRequest> consumer : consumers) {
            consumer.accept(request);
        }
        return request;
    }

}
