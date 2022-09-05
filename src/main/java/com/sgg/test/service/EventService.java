package com.sgg.test.service;

import com.sgg.test.config.EventProperties;
import com.sgg.test.controller.model.EventRequest;
import com.sgg.test.controller.model.EventType;
import com.sgg.test.controller.model.ViolationResponse;
import com.sgg.test.controller.model.ViolationSummaryResponse;
import com.sgg.test.dao.EventRepository;
import com.sgg.test.exceptions.EventNotFoundException;
import com.sgg.test.exceptions.TypeNotSupportedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventProperties eventProperties;

    public void save(EventRequest event) {
        EventType eventType = event.getEventType();
        switch (eventType) {
            case RED_LIGHT -> eventRepository.save(event);
            case SPEED -> {
                if (event.getSpeed() > event.getLimit()) eventRepository.save(event);
            }
            default -> throw new TypeNotSupportedException(format("%s not supported!", event.getEventDate()));
        }
    }

    public EventRequest findById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(format("Not found Event by id = %s", id.toString())));
    }

    public ViolationSummaryResponse getSummary() {
        ViolationSummaryResponse summary = new ViolationSummaryResponse();
        eventRepository.findAll().forEach(event -> {
            Integer fine = computeFine(event);
            Integer paidFines = summary.getTotalPaidFines();
            Integer unpaidFines = summary.getTotalUnpaidFines();
            if (event.getProcessed()) summary.setTotalPaidFines(paidFines + fine);
            else summary.setTotalUnpaidFines(unpaidFines + fine);
        });
        return summary;
    }

    public List<ViolationResponse> findAll() {
        return eventRepository.findAll()
                .stream()
                .map(event -> new ViolationResponse(event.getId(), computeFine(event), event.getProcessed()))
                .toList();
    }

    private Integer computeFine(EventRequest event) {
        switch (event.getEventType()) {
            case RED_LIGHT -> {
                return eventProperties.getFine().getRedLightRate();
            }
            case SPEED -> {
                return eventProperties.getFine().getSpeedRate();
            }
            default -> throw new TypeNotSupportedException(format("%s not supported!", event.getEventDate()));
        }
    }

}