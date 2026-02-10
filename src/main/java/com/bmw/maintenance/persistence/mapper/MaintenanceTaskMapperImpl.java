package com.bmw.maintenance.persistence.mapper;

import jakarta.enterprise.context.ApplicationScoped;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.persistence.MaintenanceTaskEntity;

/**
 * Maps between {@link MaintenanceTaskEntity} and {@link com.bmw.maintenance.domain.MaintenanceTask}.
 */
@ApplicationScoped
public class MaintenanceTaskMapperImpl implements MaintenanceTaskMapper {

    @Override
    public MaintenanceTask toDomain(MaintenanceTaskSchemaVLatest.MaintenanceTask schema) {
        return MaintenanceTask.reconstitute(
                schema.getTaskId(),
                schema.getVin(),
                schema.getType(),
                schema.getStatus(),
                schema.getNotes()
        );
    }

    @Override
    public MaintenanceTaskSchemaVLatest.MaintenanceTask toSchema(MaintenanceTask task) {
        MaintenanceTaskSchemaVLatest.MaintenanceTask schema = new MaintenanceTaskSchemaVLatest.MaintenanceTask();
        schema.setTaskId(task.getTaskId());
        schema.setVin(task.getVin());
        schema.setType(task.getType());
        schema.setStatus(task.getStatus());
        schema.setNotes(task.getNotes());

        return schema;
    }
}
