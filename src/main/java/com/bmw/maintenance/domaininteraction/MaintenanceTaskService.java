package com.bmw.maintenance.domaininteraction;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.creators.TaskCreator;
import com.bmw.maintenance.domain.enums.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

/**
 * Service for creating and managing maintenance tasks.
 */
@ApplicationScoped
public class MaintenanceTaskService {





    /**
     * Creates a maintenance task for a vehicle.
     *
     * @param vin   vehicle identification number
     * @param type  task type
     * @param notes optional notes
     * @return created task id
     */
    @Inject
    Instance<TaskCreator> creators;
    @Inject
    MaintenanceTasks maintenanceTasks;

    public Long createTask(String vin, TaskType type, String notes, Map<String, Object> additionalDetails) {

        TaskCreator creator = null;
        for (TaskCreator c : creators) {
            if (c.whatTask() == type) {
                creator = c;
                break;
            }
        }
        if (creator == null) {
            throw new BadRequestException("No creator found for task " + type);
        }
        MaintenanceTask task = creator.create(vin, notes, additionalDetails == null ? Map.of() : additionalDetails);
        return maintenanceTasks.create(task).getTaskId();


    }
    /**
     * Updates the status of a task.
     *
     * @param taskId    task id
     * @param newStatus new status
     */
    public void updateTaskStatus(String taskId, TaskStatus newStatus) {
        maintenanceTasks.updateStatus(taskId, newStatus);
    }

    /**
     * Adds or updates notes for a task.
     *
     * @param taskId task id
     * @param notes  notes to store
     */
    public void addOrUpdateNotes(String taskId, String notes) {
        maintenanceTasks.upsertNotes(taskId, notes);
    }

    /**
     * Gets a task by id.
     *
     * @param taskId task id
     * @return task or null if not found
     */
    public MaintenanceTask getTaskById(String taskId) {
        return maintenanceTasks.findById(taskId);
    }

    /**
     * Lists tasks for a VIN or all tasks when VIN is blank.
     *
     * @param vin vehicle identification number
     * @return matching tasks
     */
    public List<MaintenanceTask> listTasks(String vin) {
        if (vin != null && !vin.isBlank()) {
            return maintenanceTasks.findByVin(vin);
        }
        return maintenanceTasks.getAllTasks();
    }
}