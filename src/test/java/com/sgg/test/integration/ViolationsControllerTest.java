package com.sgg.test.integration;

import com.sgg.test.controller.model.EventRequest;
import com.sgg.test.controller.model.ViolationSummaryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static com.sgg.test.Constants.VIOLATIONS_PATH;
import static com.sgg.test.controller.model.EventType.RED_LIGHT;
import static com.sgg.test.controller.model.EventType.SPEED;
import static java.lang.String.format;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ViolationsControllerTest extends MvcAbstractTest {

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnEmptyList_WhenNothingFound_SUCCESS() throws Exception {
        mockMvc.perform(get(VIOLATIONS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnViolation_WhenItPresent_SUCCESS() throws Exception {
        String violationResponse = getFileAsString("json/violation_response.json");
        String eventRequest = getFileAsString("json/events/speed_event.json");
        EventRequest expectedEvent = mapper.readValue(eventRequest, EventRequest.class);
        repository.save(expectedEvent);

        mockMvc.perform(get(format(VIOLATIONS_PATH)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(violationResponse, false))
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnEmptySummary_WhenNothingFound_SUCCESS() throws Exception {
        mockMvc.perform(get(SUMMARIES_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(new ViolationSummaryResponse())))
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnSummary_WhenPaidFinesPresentOnly_SUCCESS() throws Exception {
        String payedOnly = getFileAsString("json/summaries/paid_response.json");
        EventRequest redLightEvent = createNewEvent(RED_LIGHT);
        redLightEvent.setProcessed(true);
        repository.save(redLightEvent);

        mockMvc.perform(get(SUMMARIES_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(payedOnly))
                .andDo(print());
    }


    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnSummary_WhenUnpaidFinesPresentOnly_SUCCESS() throws Exception {
        String unpaidOnly = getFileAsString("json/summaries/unpaid_response.json");
        repository.save(createNewEvent(SPEED));

        mockMvc.perform(get(SUMMARIES_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(unpaidOnly))
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = BEFORE_METHOD)
    void should_ReturnCorrectSummary_WhenMixedFinesPresent_SUCCESS() throws Exception {
        String summaries = getFileAsString("json/summaries/mixed_response.json");
        EventRequest redLightEvent = createNewEvent(RED_LIGHT);
        redLightEvent.setProcessed(true);
        repository.save(redLightEvent);
        repository.save(createNewEvent(SPEED));

        mockMvc.perform(get(SUMMARIES_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(summaries))
                .andDo(print());
    }

}