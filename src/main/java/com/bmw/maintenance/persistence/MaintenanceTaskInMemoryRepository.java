package com.bmw.maintenance.persistence;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.TaskStatus;
import com.bmw.maintenance.domaininteraction.MaintenanceTasks;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

/**
 * In-memory implementation of {@link MaintenanceTasks} for managing maintenance tasks.
 */
@ApplicationScoped
public class MaintenanceTaskInMemoryRepository implements MaintenanceTasks {

    private final Map<Long, MaintenanceTaskEntity> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1L);
    private final MaintenanceTaskMapper mapper;

    @Inject
    public MaintenanceTaskInMemoryRepository(MaintenanceTaskMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public MaintenanceTask create(MaintenanceTask task) {
        MaintenanceTaskEntity entity = mapper.toEntity(task);
        entity.setId(idCounter.getAndIncrement());

        storage.put(entity.getId(), entity);

        return mapper.toDomain(entity);
    }

    @Override
    public MaintenanceTask findById(String taskId) {
        Long id = Long.parseLong(taskId);
        MaintenanceTaskEntity entity = storage.get(id);

        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        return mapper.toDomain(entity);
    }

    @Override
    public MaintenanceTask updateStatus(String taskId, TaskStatus newStatus) {
        Long id = Long.parseLong(taskId);
        MaintenanceTaskEntity entity = storage.get(id);

        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        entity.setStatus(newStatus);
        entity.setUpdatedAt(LocalDateTime.now());

        storage.put(id, entity);
        return mapper.toDomain(entity);
    }

    @Override
    public MaintenanceTask upsertNotes(String taskId, String notes) {
        Long id = Long.parseLong(taskId);
        MaintenanceTaskEntity entity = storage.get(id);

        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        entity.setNotes(notes);
        entity.setUpdatedAt(LocalDateTime.now());

        storage.put(id, entity);

        return mapper.toDomain(entity);
    }

    @Override
    public List<MaintenanceTask> findAll() {
        return storage.values().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceTask> findByVin(String vin) {
        return storage.values().stream()
                .filter(entity -> vin.equals(entity.getVin()))
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

}
