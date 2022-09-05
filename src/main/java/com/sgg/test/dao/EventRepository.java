package com.sgg.test.dao;

import com.sgg.test.controller.model.EventRequest;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EventRepository {
    private final Map<UUID, EventRequest> storage = new ConcurrentHashMap<>();

    public void save(EventRequest event) {
        storage.put(event.getId(), event);
    }

    public Optional<EventRequest> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Collection<EventRequest> findAll() {
        return storage.values();
    }

}
