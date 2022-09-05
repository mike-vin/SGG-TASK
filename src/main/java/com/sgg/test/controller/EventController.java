package com.sgg.test.controller;

import com.sgg.test.controller.model.EventRequest;
import com.sgg.test.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static com.sgg.test.Constants.EVENTS_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(EVENTS_PATH)
public class EventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void saveEvent(@RequestBody @Valid EventRequest event) {
        eventService.save(event);
    }

    @GetMapping("/{id}")
    public EventRequest findById(@PathVariable UUID id) {
        return eventService.findById(id);
    }
}
