package com.bmw.maintenance.domaininteraction;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.TaskStatus;

import java.util.List;

/**
 * Defines operations for managing maintenance tasks.
 */
public interface MaintenanceTasks {
    /**
     * Creates a new maintenance task.
     *
     * @param task the task to create
     * @return the created task
     */
    MaintenanceTask create(MaintenanceTask task);

    /**
     * Updates the status of an existing task.
     *
     * @param taskId the task id
     * @param newStatus the new status
     * @return the updated task
     */
    MaintenanceTask updateStatus(String taskId, TaskStatus newStatus);

    /**
     * Inserts or updates the notes for a task.
     *
     * @param taskId the task id
     * @param notes the notes to upsert
     * @return the updated task
     */
    MaintenanceTask upsertNotes(String taskId, String notes);

    /**
     * Finds a task by id.
     *
     * @param taskId the task id
     * @return the found task
     */
    MaintenanceTask findById(String taskId);

    /**
     * Returns all tasks.
     *
     * @return the list of tasks
     */
    List<MaintenanceTask> findAll();

    /**
     * Finds tasks by VIN.
     *
     * @param vin the vehicle identification number
     * @return the list of tasks
     */
    List<MaintenanceTask> findByVin(String vin);
}
