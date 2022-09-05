package com.sgg.test.integration;

import com.sgg.test.controller.model.EventRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static com.sgg.test.Constants.EVENTS_PATH;
import static com.sgg.test.controller.model.EventType.SPEED;
import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest extends MvcAbstractTest {

    @Test
    void should_Save_SpeedEvent_WhenCreateRequestValid_SUCCESS() throws Exception {
        String eventRequest = getFileAsString("json/events/speed_event.json");
        EventRequest expectedEvent = mapper.readValue(eventRequest, EventRequest.class);

        mockMvc.perform(post(EVENTS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventRequest))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get(format(EVENTS_BY_ID_PATH, expectedEvent.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(eventRequest, false))
                .andDo(print());
    }

    @Test
    void should_Save_RedLightEvent_WhenCreateRequestValid_SUCCESS() throws Exception {
        String eventRequest = getFileAsString("json/events/red_light_event.json");
        EventRequest expectedEvent = mapper.readValue(eventRequest, EventRequest.class);

        mockMvc.perform(post(EVENTS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventRequest))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get(format(EVENTS_BY_ID_PATH, expectedEvent.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(eventRequest, false))
                .andDo(print());
    }

    @Test
    void should_NotSave_WhenCreateRequestValid_SUCCESS() throws Exception {
        EventRequest speedEvent = createNewEvent(SPEED);
        speedEvent.setSpeed(speedEvent.getSpeed() / 2);
        String eventJson = mapper.writeValueAsString(speedEvent);

        mockMvc.perform(post(EVENTS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(get(format(EVENTS_BY_ID_PATH, speedEvent.getId())))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void should_ReturnBadRequest_WhenCreateRequestHasInvalidEventType_SUCCESS() throws Exception {
        String unknownTypeEvent = getFileAsString("json/events/unknown_event.json");

        mockMvc.perform(post(EVENTS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unknownTypeEvent))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void should_ReturnBadRequest_WhenRequestHasInvalidEventBody_FAIL() throws Exception {
        String invalidRequest = mapper.writeValueAsString(new EventRequest());

        mockMvc.perform(post(EVENTS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void should_ReturnNotFound_WhenEventNotFoundById_FAIL() throws Exception {
        mockMvc.perform(get(format(EVENTS_BY_ID_PATH, UUID.randomUUID())))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
