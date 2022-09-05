package com.sgg.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgg.test.Application;
import com.sgg.test.controller.model.EventRequest;
import com.sgg.test.controller.model.EventType;
import com.sgg.test.dao.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.sgg.test.Constants.EVENTS_PATH;
import static com.sgg.test.Constants.VIOLATIONS_PATH;
import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = Application.class)
public abstract class MvcAbstractTest {

    protected static final String SUMMARIES_PATH = VIOLATIONS_PATH + "/summaries";
    protected static final String EVENTS_BY_ID_PATH = EVENTS_PATH.concat("/%s");

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    protected EventRepository repository;
    @Autowired
    protected WebApplicationContext webContext;

    protected EventRequest createNewEvent(EventType eventType) {
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


    protected String getFileAsString(String path) {
        Resource resource = webContext.getResource("classpath:" + path);
        try (InputStream stream = resource.getInputStream()) {
            Reader reader = new InputStreamReader(stream, UTF_8);
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
