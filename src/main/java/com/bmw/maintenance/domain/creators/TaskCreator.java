package com.bmw.maintenance.domain.creators;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.enums.TaskType;

import java.util.Map;

public interface TaskCreator {
    TaskType whatTask();
    MaintenanceTask create(String vin, String notes, Map<String, Object> data);
}
