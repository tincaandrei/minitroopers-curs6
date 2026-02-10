package com.bmw.maintenance.persistence;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistence entity for maintenance tasks.
 * <p>
 * This entity serves as an envelope for storing maintenance task aggregates
 * in their serialized form, along with metadata about when the entity was
 * created and last updated.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceTaskEntity {

    private Long id;

    /** Serialized representation of the maintenance task aggregate. */
    private String aggregate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
