package com.bmw.maintenance.domaininteraction;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.TaskStatus;
import com.bmw.maintenance.domain.TaskType;

import java.util.List;
import java.util.Objects;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MaintenanceTaskService {

    private final MaintenanceTasks maintenanceTasks;

    public MaintenanceTaskService(MaintenanceTasks maintenanceTasks) {
        this.maintenanceTasks = Objects.requireNonNull(maintenanceTasks, "maintenanceTasks must not be null");
    }

    public Long createTask(String vin, TaskType type, String notes) {
        MaintenanceTask task = switch (type) {
            case OIL_CHANGE -> MaintenanceTask.createOilChange(vin, notes);
            case BRAKE_INSPECTION -> MaintenanceTask.createBrakeInspection(vin, notes);
        };

        MaintenanceTask created = maintenanceTasks.create(task);
        return created.getTaskId();
    }

    public void updateTaskStatus(String taskId, TaskStatus newStatus) {
        maintenanceTasks.updateStatus(taskId, newStatus);
    }

    public void addOrUpdateNotes(String taskId, String notes) {
        maintenanceTasks.upsertNotes(taskId, notes);
    }

    public MaintenanceTask getTaskById(String taskId) {
        return maintenanceTasks.findById(taskId);
    }

    public List<MaintenanceTask> listTasks(String vin) {
        if (vin != null && !vin.isBlank()) {
            return maintenanceTasks.findByVin(vin);
        }
        return maintenanceTasks.findAll();
    }
}
