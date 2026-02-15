package com.bmw.maintenance.domain.creators;

import com.bmw.maintenance.domain.MaintenanceTask;
import com.bmw.maintenance.domain.enums.ScannerType;
import com.bmw.maintenance.domain.enums.TaskType;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DiagnosticScanCreator implements TaskCreator{
    @Override
    public TaskType whatTask() {
        return TaskType.DIAGNOSTIC_SCAN;
    }

    @Override
    public MaintenanceTask create(String vin, String notes, Map<String, Object> data) {
        Log.info("RECEIVED DATA IS " + data);

        Object rawCodes = data.get("errorCodes");
        if(!(rawCodes instanceof List<?> list)){
            throw new BadRequestException("Error codes must be a list");
        }
        List<String> codes = list.stream()
                .map(String::valueOf)
                .toList();
        String type = (String) data.get("scannerType");
        if(type == null){
            throw new BadRequestException("Maintenance type is required for this operation");
        }
        ScannerType scannerType = ScannerType.valueOf(type);

        return MaintenanceTask.createDiagnosticScan(vin, notes, codes, scannerType);
    }
}
