package com.bmw.maintenance.domaininteraction;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface MaintenanceTasks {
    MaintenanceTask create(MaintenanceTask task);

    MaintenanceTask updateStatus(String taskId, TaskStatus newStatus);

    MaintenanceTask upsertNotes(String taskId, String notes);

    MaintenanceTask findById(String taskId);

    List<MaintenanceTask> findAll();

    List<MaintenanceTask> findByVin(String vin);
}
