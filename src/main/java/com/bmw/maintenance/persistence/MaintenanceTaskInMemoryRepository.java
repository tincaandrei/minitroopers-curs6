package com.bmw.maintenance.persistence;

import com.bmw.maintenance.commons.serialization.VersionedSchemaSerDes;
import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.TaskStatus;
import com.bmw.maintenance.domaininteraction.MaintenanceTasks;
import com.bmw.maintenance.persistence.mapper.MaintenanceTaskMapper;
import com.bmw.maintenance.persistence.mapper.MaintenanceTaskSchemaVLatest;

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
    private final VersionedSchemaSerDes<String> serializer;

    @Inject
    public MaintenanceTaskInMemoryRepository(MaintenanceTaskMapper mapper, VersionedSchemaSerDes<String> serializer) {
        this.mapper = mapper;
        this.serializer = serializer;
    }

    @Override
    public MaintenanceTask create(MaintenanceTask task) {
        Long id = idCounter.getAndIncrement();

        // Map domain to schema
        MaintenanceTaskSchemaVLatest.MaintenanceTask schema = mapper.toSchema(task);
        schema.setTaskId(id);

        // Serialize schema
        String serializedAggregate = serializer.serialize(schema);

        // Create entity with serialized aggregate
        MaintenanceTaskEntity entity = new MaintenanceTaskEntity();
        entity.setId(id);
        entity.setAggregate(serializedAggregate);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        storage.put(entity.getId(), entity);

        return mapper.toDomain(schema);
    }

    @Override
    public MaintenanceTask findById(String taskId) {
        Long id = Long.parseLong(taskId);
        MaintenanceTaskEntity entity = storage.get(id);

        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        // Deserialize aggregate to schema
        MaintenanceTaskSchemaVLatest.MaintenanceTask schema =
            (MaintenanceTaskSchemaVLatest.MaintenanceTask) serializer.deserialize(entity.getAggregate());

        return mapper.toDomain(schema);
    }

    @Override
    public MaintenanceTask updateStatus(String taskId, TaskStatus newStatus) {
        Long id = Long.parseLong(taskId);
        MaintenanceTaskEntity entity = storage.get(id);

        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        // Deserialize aggregate to schema
        MaintenanceTaskSchemaVLatest.MaintenanceTask schema =
            (MaintenanceTaskSchemaVLatest.MaintenanceTask) serializer.deserialize(entity.getAggregate());

        // Update schema
        schema.setStatus(newStatus);

        // Serialize updated schema
        String serializedAggregate = serializer.serialize(schema);
        entity.setAggregate(serializedAggregate);
        entity.setUpdatedAt(LocalDateTime.now());

        storage.put(id, entity);
        return mapper.toDomain(schema);
    }

    @Override
    public MaintenanceTask upsertNotes(String taskId, String notes) {
        Long id = Long.parseLong(taskId);
        MaintenanceTaskEntity entity = storage.get(id);

        if (entity == null) {
            throw new NotFoundException("Task not found: " + taskId);
        }

        // Deserialize aggregate to schema
        MaintenanceTaskSchemaVLatest.MaintenanceTask schema =
            (MaintenanceTaskSchemaVLatest.MaintenanceTask) serializer.deserialize(entity.getAggregate());

        // Update schema
        schema.setNotes(notes);

        // Serialize updated schema
        String serializedAggregate = serializer.serialize(schema);
        entity.setAggregate(serializedAggregate);
        entity.setUpdatedAt(LocalDateTime.now());

        storage.put(id, entity);

        return mapper.toDomain(schema);
    }

    @Override
    public List<MaintenanceTask> getAllTasks() {
        return storage.values().stream()
                .map(entity -> {
                    MaintenanceTaskSchemaVLatest.MaintenanceTask schema =
                        (MaintenanceTaskSchemaVLatest.MaintenanceTask) serializer.deserialize(entity.getAggregate());
                    return mapper.toDomain(schema);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceTask> findByVin(String vin) {
        return storage.values().stream()
                .map(entity -> {
                    MaintenanceTaskSchemaVLatest.MaintenanceTask schema =
                        (MaintenanceTaskSchemaVLatest.MaintenanceTask) serializer.deserialize(entity.getAggregate());
                    return mapper.toDomain(schema);
                })
                .filter(task -> vin.equals(task.getVin()))
                .collect(Collectors.toList());
    }

}
