package com.sgg.test.controller.model;

import java.util.UUID;

public record ViolationResponse(UUID eventId, Integer fine, Boolean paid) {
}
