package com.bmw.maintenance.domain.creators;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.enums.TaskType;
import com.bmw.maintenance.domain.enums.TirePosition;
import com.bmw.maintenance.domain.enums.TireServiceType;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

import java.util.Map;

@ApplicationScoped
public class TireServiceCreator implements TaskCreator {
    @Override
    public TaskType whatTask() {
        return TaskType.TIRE_SERVICE;
    }

    @Override
    public MaintenanceTask create(String vin, String notes, Map<String, Object> data) {
        Log.info("CURRENT SERVICE DATA IS " + data);

        String pos = (String) data.get("tirePosition");
        if(pos == null){
            throw new BadRequestException("tire position is required for this action");
        }
        TirePosition position = TirePosition.valueOf(pos);


        String type = (String) data.get("tireServiceType");

//        Log.info("CURRENT SERVICE CREATOR TYPE IS " + type);
        if(type== null){
            throw new BadRequestException("Operation type is required for this action");
        }
        TireServiceType serviceType = TireServiceType.valueOf(type);

        return MaintenanceTask.createTireService(vin, notes, position, serviceType);
    }
}
