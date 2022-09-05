package com.sgg.test.controller;

import com.sgg.test.controller.model.ViolationResponse;
import com.sgg.test.controller.model.ViolationSummaryResponse;
import com.sgg.test.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.sgg.test.Constants.VIOLATIONS_PATH;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(VIOLATIONS_PATH)
public class ViolationsController {

    private final EventService eventService;

    @GetMapping
    public List<ViolationResponse> findAll() {
        return eventService.findAll();
    }

    @GetMapping("/summaries")
    public ViolationSummaryResponse getSummary() {
        return eventService.getSummary();
    }

}
