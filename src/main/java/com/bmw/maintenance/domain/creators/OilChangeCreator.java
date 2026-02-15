package com.bmw.maintenance.domain.creators;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.enums.TaskType;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
public class OilChangeCreator implements TaskCreator {
    @Override
    public TaskType whatTask() {
        return TaskType.OIL_CHANGE;
    }

    @Override
    public MaintenanceTask create(String vin, String notes, Map<String, Object> data) {
         return MaintenanceTask.createOilChange(vin, notes);
    }
}
