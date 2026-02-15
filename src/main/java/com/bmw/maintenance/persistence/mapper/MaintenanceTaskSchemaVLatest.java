package com.bmw.maintenance.persistence.mapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.bmw.maintenance.commons.serialization.VersionedSchema;
import com.bmw.maintenance.domain.enums.TaskStatus;
import com.bmw.maintenance.domain.enums.TaskType;

/**
 * Contains schema definitions for maintenance task persistence.
 * <p>
 * These schemas are used as an intermediate representation between domain objects
 * and their serialized form, enabling schema versioning and migration.
 * </p>
 */
public interface MaintenanceTaskSchemaVLatest {

    /**
     * Persistence schema for a maintenance task.
     * <p>
     * This schema represents version 1 of the maintenance task data structure
     * used for serialization and storage.
     * </p>
     */
    @Data
    @NoArgsConstructor
    class MaintenanceTask implements VersionedSchema {

        static final int SCHEMA_VERSION = 1;

        private Long taskId;
        private String vin;
        private TaskType type;
        private TaskStatus status;
        private String notes;

        @Override
        public int schemaVersion() {
            return SCHEMA_VERSION;
        }
    }

}
